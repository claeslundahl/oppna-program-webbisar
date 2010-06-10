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

import static org.apache.commons.lang.StringUtils.*;

import java.io.Serializable;
import java.util.List;

import se.vgregion.webbisar.types.Name;
import se.vgregion.webbisar.types.Sex;
import se.vgregion.webbisar.types.Webbis;

public class SearchResultBean implements Serializable {

    private static final long serialVersionUID = 1L;

    Long id;
    String header;
    String info;

    public SearchResultBean(Webbis webbis) {
        this.id = webbis.getId();
        this.header = createHeader(webbis);
        this.info = createInfo(webbis);
    }

    public Long getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public String getInfo() {
        return info;
    }

    private String createHeader(Webbis webbis) {
        StringBuffer sb = new StringBuffer();

        List<Name> parents = webbis.getParents();
        if (parents.size() == 0)
            return "Länk till webbisen";
        if (parents.size() == 1) {
            sb.append(parents.get(0).getFullName());
        } else {
            sb.append(parents.get(0).getFullName() + " och " + parents.get(1).getFullName());
        }
        if (isNotEmpty(webbis.getHome())) {
            sb.append(", ").append(webbis.getHome());
        }
        sb.append(", ").append(webbis.getHospital().getLongName());
        return sb.toString();
    }

    private String createInfo(Webbis webbis) {
        StringBuffer sb = new StringBuffer();
        if (isNotEmpty(webbis.getName())) {
            sb.append(webbis.getName());
        } else {
            sb.append("En ").append(webbis.getSex() == Sex.Female ? "flicka" : "pojke");
        }
        sb.append(", ").append(webbis.getBirthTime());
        sb.append(", ").append(webbis.getWeight()).append("g");
        sb.append(", ").append(webbis.getLength()).append("cm");
        return sb.toString();
    }

}
