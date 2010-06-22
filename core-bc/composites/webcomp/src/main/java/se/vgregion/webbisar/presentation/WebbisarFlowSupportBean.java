/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.webbisar.presentation;

import java.io.File;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.LocalParameterMap;

import se.vgregion.webbisar.presentation.exceptions.WebbisNotFoundException;
import se.vgregion.webbisar.svc.Configuration;
import se.vgregion.webbisar.svc.WebbisService;
import se.vgregion.webbisar.types.Webbis;

public class WebbisarFlowSupportBean {

    private static final Log LOGGER = LogFactory.getLog(WebbisarFlowSupportBean.class);

    private static final Pattern RFC2822_MAIL_PATTERN = Pattern
            .compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

    private static final String ENCODING_UTF8 = "UTF-8";
    private static final int NUMBER_OF_WEBBIS_ON_BROWSE_PAGE = 6;
    private WebbisService webbisService;
    private Configuration cfg;
    private JavaMailSender mailSender;
    private VelocityEngine velocityEngine;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public WebbisService getAddressService() {
        return webbisService;
    }

    public void setWebbisService(WebbisService webbisService) {
        this.webbisService = webbisService;
    }

    public void setConfiguration(Configuration cfg) {
        this.cfg = cfg;
    }

    public boolean shouldShowWebbis(String webbisId) {
        return !StringUtils.isBlank(webbisId);
    }

    public WebbisPageBean loadPage(WebbisPageBean o) {
        int pageNumber = (o == null) ? 0 : (o).getPageNumber();
        return internalLoadPage(pageNumber);
    }

    public WebbisPageBean loadNextPage(WebbisPageBean o) {
        int pageNumber = (o == null) ? 0 : (o).getPageNumber() + 1;
        return internalLoadPage(pageNumber);
    }

    public WebbisPageBean loadPrevPage(WebbisPageBean o) {
        int pageNumber = (o == null) ? 0 : ((o).getPageNumber() == 0) ? 0 : (o).getPageNumber() - 1;

        return internalLoadPage(pageNumber);
    }

    protected WebbisPageBean internalLoadPage(int pageNumber) {
        long numberOfWebbisar = webbisService.getNumberOfWebbisar();

        long numberOfPages = numberOfWebbisar / NUMBER_OF_WEBBIS_ON_BROWSE_PAGE
                + ((numberOfWebbisar % NUMBER_OF_WEBBIS_ON_BROWSE_PAGE) != 0 ? 1 : 0);

        List<Webbis> webbisar = webbisService.getWebbisar(pageNumber * NUMBER_OF_WEBBIS_ON_BROWSE_PAGE,
                NUMBER_OF_WEBBIS_ON_BROWSE_PAGE);

        List<WebbisBean> list = new ArrayList<WebbisBean>();
        for (Webbis webbis : webbisar) {
            list.add(new WebbisBean(cfg.getMultimediaFileBaseUrl(), webbis, 0));
        }
        return new WebbisPageBean(pageNumber, pageNumber == 0, pageNumber == (numberOfPages - 1), list);
    }

    public WebbisBean getWebbis(final Long webbisId, final Integer selectedImage,
            final ExternalContext externalContext, LocalParameterMap parameterMap,
            SearchCriteriaBean searchCriteria) throws WebbisNotFoundException {
        int imageId = (selectedImage == null) ? 0 : selectedImage;

        // Get webbis
        Webbis webbis = webbisService.getById(webbisId);

        // No webbis found, set 404 and throw error
        if (webbis == null) {
            if (externalContext != null && externalContext.getNativeResponse() instanceof HttpServletResponse) {
                ((HttpServletResponse) externalContext.getNativeResponse())
                        .setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            throw new WebbisNotFoundException("Webbis med id " + webbisId + " finns inte.");
        }

        // Create bean
        WebbisBean webbisBean = new WebbisBean(cfg.getMultimediaFileBaseUrl(), webbis, imageId);

        // If we came here via search engine direct link, also populate serachCriteria
        if (parameterMap != null && parameterMap.contains("q")) {
            StringBuilder searchEngineQuerySB = getSearchEngineQueryString(parameterMap);

            searchCriteria.setSearchEngineQueryParameters(searchEngineQuerySB.toString());
        }

        return webbisBean;
    }

    public MailMessageResultBean sendWebbis(final Long webbisId, final MailMessageBean mailMessageBean)
            throws WebbisNotFoundException {

        // Validate email adresses first
        MailMessageResultBean result = validateEmailAddresses(mailMessageBean);
        if (Boolean.FALSE.equals(result.getSuccess())) {
            return result;
        }
        // Validate sender name
        if (StringUtils.isBlank(mailMessageBean.getSenderName())) {
            result.setSuccess(Boolean.FALSE);
            result.setMessage("Namn på avsändare måste anges.");
            return result;
        }

        // use this map to store the information that will be merged into the html template
        Map<String, String> emailInformation = new HashMap<String, String>();
        WebbisBean webbisBean = getWebbis(webbisId, null, null, null, null);
        Map<Long, String> webbisarIdNames = webbisBean.getMultipleBirthSiblingIdsAndNames();

        String messageText = mailMessageBean.getMessage();
        if (!StringUtils.isEmpty(messageText)) {
            messageText = messageText.replace("\r", "").replace("\n", "<br/>");
        }

        // add the current webbis to the list of siblings so that
        // we have them all in the same Map
        webbisarIdNames.put(webbisBean.getId(), webbisBean.getName());

        // add the message and the base url for html links
        emailInformation.put("baseUrl", cfg.getBaseUrl());
        emailInformation.put("message", messageText);
        emailInformation.put("senderName", mailMessageBean.getSenderName());
        emailInformation.put("senderAddress", mailMessageBean.getSenderAddress());

        VelocityContext context = new VelocityContext();
        context.put("emailInfo", emailInformation);
        context.put("webbisInfo", webbisarIdNames);

        Template template = null;
        StringWriter msgWriter = null;
        try {
            velocityEngine.init();
            template = velocityEngine.getTemplate(cfg.getMailTemplate());
            msgWriter = new StringWriter();
            template.merge(context, msgWriter);
        } catch (Exception e1) {
            LOGGER.error("Failed to get/merge velocity template.", e1);
            result.setSuccess(Boolean.FALSE);
            result.setMessage("Internt fel, webbis kunde inte skickas.");
            return result;
        }
        String msgText = msgWriter.toString();

        // Seems OK, try to send mail...
        try {
            InternetAddress fromAddress = null;
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, ENCODING_UTF8);
            try {
                fromAddress = new InternetAddress(cfg.getMailFromAddress(), cfg.getMailFromAddressName());
            } catch (UnsupportedEncodingException e) {
                fromAddress = new InternetAddress(cfg.getMailFromAddress());
            }
            helper.setTo(mailMessageBean.getRecipientAddresses().split(","));
            helper.setFrom(fromAddress);
            helper.setSubject(mailMessageBean.getSubject());
            helper.setText(msgText, true);

            // include the vgr logo
            String logoPath = cfg.getMultimediaFileBaseDir() + "/" + cfg.getMailLogo();
            FileSystemResource res = new FileSystemResource(new File(logoPath));
            helper.addInline("imageIdentifier", res);

            mailSender.send(mimeMessage);
        } catch (MailException ex) {
            LOGGER.error("Failed to create/send mail.", ex);
            result.setSuccess(Boolean.FALSE);
            result.setMessage("Internt fel, webbis kunde inte skickas.");
            return result;
        } catch (MessagingException e) {
            LOGGER.error("Failed to create/send mail.", e);
            result.setSuccess(Boolean.FALSE);
            result.setMessage("Internt fel, webbis kunde inte skickas.");
            return result;
        }

        // ...and all was well...
        result.setSuccess(Boolean.TRUE);
        result.setMessage("Webbis skickad!");
        return result;
    }

    /**
     * Will add q= before search engine query string if not already included
     * 
     * @param searchCriteria
     * @return SearchCriteriaBean with search engine query URL parameters
     * @throws UnsupportedEncodingException
     */
    public SearchCriteriaBean search(SearchCriteriaBean searchCriteria) throws UnsupportedEncodingException {
        // Copy to searchEngineQueryParameters, a hidden field that will not be displayed
        if (!StringUtils.isBlank(searchCriteria.getText())) {
            searchCriteria.setSearchEngineQueryParameters(searchCriteria.getText());
        }
        // Ensure we have q= before search string query parameter
        if (searchCriteria.getSearchEngineQueryParameters() != null
                && !searchCriteria.getSearchEngineQueryParameters().contains("q=")) {
            searchCriteria.setSearchEngineQueryParameters("q=" + searchCriteria.getSearchEngineQueryParameters());
        }

        return searchCriteria;
    }

    @SuppressWarnings("unchecked")
    private StringBuilder getSearchEngineQueryString(LocalParameterMap parameterMap) {
        String parmName = null;
        String parmValue = null;
        StringBuilder searchEngineQuerySB = new StringBuilder();
        Iterator<Map.Entry<String, String>> i = parameterMap.asMap().entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, String> entry = i.next();
            parmName = entry.getKey();
            if ("webbisId".equals(parmName)) {
                // Don't append webbisId, that is not part of the search engine query
                continue;
            }
            parmValue = entry.getValue();

            if (searchEngineQuerySB.length() > 0) {
                searchEngineQuerySB.append("&");
            }
            searchEngineQuerySB.append(parmName);
            searchEngineQuerySB.append("=");

            try {
                parmValue = URLEncoder.encode(parmValue, ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Failed to URL encode search engine parameter value = " + parmValue, e);
            }
            searchEngineQuerySB.append(parmValue);
        }
        return searchEngineQuerySB;
    }

    private MailMessageResultBean validateEmailAddresses(MailMessageBean mailMessageBean) {

        MailMessageResultBean result = new MailMessageResultBean();
        result.setSuccess(Boolean.TRUE);

        // Do proper validation
        if (StringUtils.isBlank(mailMessageBean.getRecipientAddresses())) {
            // No recipient(s) supplied
            result.setSuccess(Boolean.FALSE);
            result.setMessage("Minst en mottagare måste anges.");
        } else if (StringUtils.isBlank(mailMessageBean.getSenderAddress())) {
            // No sender supplied
            result.setSuccess(Boolean.FALSE);
            result.setMessage("Avsändaradress måste anges.");
        } else {
            // Validate format for email addresses
            String[] mailAddresses = mailMessageBean.getRecipientAddresses().split(",");
            for (String mailAddress : mailAddresses) {
                if (!RFC2822_MAIL_PATTERN.matcher(mailAddress).matches()) {
                    result.setSuccess(Boolean.FALSE);
                    result.setMessage("Mottagaradressen " + mailAddress + " är inte en giltig mailadress.");
                    break;
                }
            }
            if (!RFC2822_MAIL_PATTERN.matcher(mailMessageBean.getSenderAddress()).matches()) {
                result.setSuccess(Boolean.FALSE);
                result.setMessage("Avsändaradressen " + mailMessageBean.getSenderAddress()
                        + " är inte en giltig mailadress.");
            }
        }

        return result;
    }
}
