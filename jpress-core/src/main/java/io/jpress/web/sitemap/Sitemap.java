/**
 * Copyright (c) 2016-2019, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.web.sitemap;


import io.jboot.utils.StrUtil;
import io.jboot.web.controller.JbootControllerContext;
import io.jpress.JPressConsts;
import io.jpress.JPressOptions;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

public class Sitemap implements Serializable {

    public static final String CHANGEFREQ_DAILY = "daily";
    public static final String CHANGEFREQ_WEEKLY = "weekly";
    public static final String CHANGEFREQ_MONTHLY = "monthly";
    public static final String CHANGEFREQ_ALWAYS = "always";

    private String loc;
    private Date lastmod;
    private String changefreq;
    private Float priority;

    public Sitemap() {

    }

    public Sitemap(String loc, String lastmod) {
        this.loc = loc;
        this.lastmod = SitemapUtil.str2date(lastmod);
    }

    public Sitemap(String loc, Date lastmod) {
        this.loc = loc;
        this.lastmod = lastmod;
    }

    public Sitemap(String loc, String lastmod, String changefreq, Float priority) {
        this.loc = loc;
        this.lastmod = SitemapUtil.str2date(lastmod);
        this.changefreq = changefreq;
        this.priority = priority;
    }

    public Sitemap(String loc, Date lastmod, String changefreq, Float priority) {
        this.loc = loc;
        this.lastmod = lastmod;
        this.changefreq = changefreq;
        this.priority = priority;
    }

    public String getLoc() {
        return loc;
    }

    public String getLocWithDomain() {
        if (StrUtil.isBlank(loc) || loc.startsWith("http:") || loc.startsWith("https:")) {
            return loc;
        }

        String domain = JPressOptions.get(JPressConsts.OPTION_WEB_DOMAIN, "");
        if (StrUtil.isBlank(domain) && JbootControllerContext.get() != null) {
            domain = getDefaultDomain(JbootControllerContext.get().getRequest());
        }
        return domain + loc;
    }

    private String getDefaultDomain(HttpServletRequest req) {
        int port = req.getServerPort();
        StringBuilder defaultDomain = new StringBuilder(req.getScheme());
        defaultDomain.append("://")
                .append(req.getServerName())
                .append(port == 80 ? "" : ":" + port)
                .append(req.getContextPath());
        return defaultDomain.toString();
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Date getLastmod() {
        if (lastmod == null) {
            lastmod = new Date();
        }
        return lastmod;
    }

    public void setLastmod(Date lastmod) {
        this.lastmod = lastmod;
    }

    public String getChangefreq() {
        return changefreq;
    }

    public void setChangefreq(String changefreq) {
        this.changefreq = changefreq;
    }

    public Float getPriority() {
        return priority;
    }

    public void setPriority(Float priority) {
        this.priority = priority;
    }

    public String toXml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<sitemap>");
        xmlBuilder.append("<loc>" + getLocWithDomain() + "</loc>");
        xmlBuilder.append("<lastmod>" + SitemapUtil.date2str(lastmod) + "</lastmod>");
        xmlBuilder.append("</sitemap>");
        return xmlBuilder.toString();
    }

    public String toUrlXml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<url>");
        xmlBuilder.append("<loc>" + getLocWithDomain() + "</loc>");
        xmlBuilder.append("<lastmod>" + SitemapUtil.date2str(lastmod) + "</lastmod>");
        xmlBuilder.append("<changefreq>" + changefreq + "</changefreq>");
        xmlBuilder.append("<priority>" + (priority == 1 ? "1" : priority) + "</priority>");
        xmlBuilder.append("</url>");
        return xmlBuilder.toString();
    }
}
