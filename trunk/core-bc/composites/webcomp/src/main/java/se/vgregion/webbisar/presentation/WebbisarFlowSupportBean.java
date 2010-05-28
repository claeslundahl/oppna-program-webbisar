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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import se.vgregion.webbisar.svc.Configuration;
import se.vgregion.webbisar.svc.WebbisService;
import se.vgregion.webbisar.types.Webbis;

public class WebbisarFlowSupportBean {

    private static final Pattern RFC2822_MAIL_PATTERN = Pattern
            .compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

    private static final String ENCODING_UTF8 = "UTF-8";
    private static final int NUMBER_OF_WEBBIS_ON_BROWSE_PAGE = 6;
    private static final int NUMBER_OF_WEBBIS_ON_SEARCH_PAGE = 8;
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
            list.add(new WebbisBean(cfg.getImageBaseUrl(), webbis, 0));
        }
        return new WebbisPageBean(pageNumber, pageNumber == 0, pageNumber == (numberOfPages - 1), list);
    }

    public WebbisBean getWebbis(final Long webbisId, final Integer selectedImage) {
        int imageId = (selectedImage == null) ? 0 : selectedImage;

        Webbis webbis = webbisService.getById(webbisId);

        return new WebbisBean(cfg.getImageBaseUrl(), webbis, imageId);
    }

    public MailMessageResultBean sendWebbis(final Long webbisId, final MailMessageBean mailMessageBean) {

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
        WebbisBean webbisBean = getWebbis(webbisId, null);
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
            System.err.println(e1.getMessage());
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
            String logoPath = cfg.getImageBaseDir() + "/" + cfg.getMailLogo();
            FileSystemResource res = new FileSystemResource(new File(logoPath));
            helper.addInline("imageIdentifier", res);

            mailSender.send(mimeMessage);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
            result.setSuccess(Boolean.FALSE);
            result.setMessage("Internt fel, webbis kunde inte skickas.");
            return result;
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
            result.setSuccess(Boolean.FALSE);
            result.setMessage("Internt fel, webbis kunde inte skickas.");
            return result;
        }

        // ...and all was well...
        result.setSuccess(Boolean.TRUE);
        result.setMessage("Webbis skickad!");
        return result;
    }

    public SearchResultPageBean search(SearchCriteriaBean searchCriteria) {
        int numberOfHits = webbisService.getNumberOfMatchesFor(searchCriteria.getText());

        int numberOfPages = numberOfHits / NUMBER_OF_WEBBIS_ON_SEARCH_PAGE
                + ((numberOfHits % NUMBER_OF_WEBBIS_ON_SEARCH_PAGE) != 0 ? 1 : 0);

        List<Webbis> resultList = webbisService.searchWebbisar(searchCriteria.getText(), 0,
                NUMBER_OF_WEBBIS_ON_SEARCH_PAGE);
        List<SearchResultBean> list = new ArrayList<SearchResultBean>();
        for (Webbis webbis : resultList) {
            list.add(new SearchResultBean(webbis));
        }
        return new SearchResultPageBean(searchCriteria, numberOfHits, 0, true, numberOfPages < 2, list);
    }

    private SearchResultPageBean internalLoadSearchPage(SearchCriteriaBean searchCriteria, int pageNumber) {
        int numberOfHits = webbisService.getNumberOfMatchesFor(searchCriteria.getText());
        int numberOfPages = numberOfHits / NUMBER_OF_WEBBIS_ON_SEARCH_PAGE
                + ((numberOfHits % NUMBER_OF_WEBBIS_ON_SEARCH_PAGE) != 0 ? 1 : 0);

        List<Webbis> resultList = webbisService.searchWebbisar(searchCriteria.getText(), pageNumber
                * NUMBER_OF_WEBBIS_ON_SEARCH_PAGE, NUMBER_OF_WEBBIS_ON_SEARCH_PAGE);
        List<SearchResultBean> list = new ArrayList<SearchResultBean>();
        for (Webbis webbis : resultList) {
            list.add(new SearchResultBean(webbis));
        }
        return new SearchResultPageBean(searchCriteria, numberOfHits, pageNumber, pageNumber == 0,
                pageNumber == (numberOfPages - 1), list);
    }

    public SearchResultPageBean loadNextSearchPage(SearchResultPageBean o) {
        int pageNumber = o.getPageNumber() + 1;
        return internalLoadSearchPage(o.getSearchCriteria(), pageNumber);
    }

    public SearchResultPageBean loadPrevSearchPage(SearchResultPageBean o) {
        int pageNumber = o.getPageNumber() - 1;
        return internalLoadSearchPage(o.getSearchCriteria(), pageNumber);
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
