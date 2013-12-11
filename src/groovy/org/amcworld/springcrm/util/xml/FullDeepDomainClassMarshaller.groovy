/*
 * FullDeepDomainClassMarshaller.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.amcworld.springcrm.util.xml

import grails.converters.XML
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.codehaus.groovy.grails.support.proxy.ProxyHandler
import org.codehaus.groovy.grails.web.converters.ConverterUtil
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import org.codehaus.groovy.grails.web.converters.marshaller.xml.DeepDomainClassMarshaller
import org.springframework.beans.BeanWrapper
import org.springframework.beans.BeanWrapperImpl


/**
 * The class {@code FullDeepDomainClassMarshaller} represents a marshaller for
 * domain model classes that not only exports persistent properties but also
 * properties marked with annotation {@code XmlExport}.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class FullDeepDomainClassMarshaller extends DeepDomainClassMarshaller {

    //-- Instance variables ---------------------

    protected GrailsApplication application
    protected boolean includeVersion
    protected ProxyHandler proxyHandler


    //-- Constructors ---------------------------

    FullDeepDomainClassMarshaller(GrailsApplication application) {
        this(false, application)
    }

    FullDeepDomainClassMarshaller(boolean includeVersion,
                                  GrailsApplication application)
    {
        super(includeVersion, application)
        this.includeVersion = includeVersion
        this.application = application
    }

    FullDeepDomainClassMarshaller(boolean includeVersion,
                                  ProxyHandler proxyHandler,
                                  GrailsApplication application)
    {
        this(includeVersion, application)
        this.proxyHandler = proxyHandler
    }


    //-- Public methods -------------------------

    @Override
    void marshalObject(Object value, XML xml) throws ConverterException {
        Class clazz = value.getClass()
        GrailsDomainClass domainClass = application.getArtefact(
             DomainClassArtefactHandler.TYPE,
             ConverterUtil.trimProxySuffix(clazz.name)
        )
        BeanWrapper beanWrapper = new BeanWrapperImpl(value)

        GrailsDomainClassProperty id = domainClass.identifier
        Object idValue = beanWrapper.getPropertyValue(id.name)

        if (idValue != null) xml.attribute("id", String.valueOf(idValue));

        if (includeVersion) {
            Object versionValue =
                beanWrapper.getPropertyValue(domainClass.version.name)
            xml.attribute("version", String.valueOf(versionValue))
        }

        for (GrailsDomainClassProperty property : domainClass.properties) {
            if (!includeInXml(property)) continue

            xml.startNode property.name
            if (!property.association) {
                xml.convertAnother beanWrapper.getPropertyValue(property.name)
            } else {
                Object referenceObject = beanWrapper.getPropertyValue(property.name)
                if (isRenderDomainClassRelations()) {
                    if (referenceObject != null) {
                        referenceObject = proxyHandler ? proxyHandler.unwrapIfProxy(referenceObject) : referenceObject
                        if (referenceObject instanceof SortedMap) {
                            referenceObject = new TreeMap((SortedMap) referenceObject)
                        } else if (referenceObject instanceof SortedSet) {
                            referenceObject = new TreeSet((SortedSet) referenceObject)
                        } else if (referenceObject instanceof Set) {
                            referenceObject = new HashSet((Set) referenceObject)
                        } else if (referenceObject instanceof Map) {
                            referenceObject = new HashMap((Map) referenceObject)
                        } else if (referenceObject instanceof Collection) {
                            referenceObject = new ArrayList((Collection) referenceObject)
                        }
                        xml.convertAnother referenceObject
                    }
                } else {
                    if (referenceObject != null) {
                        GrailsDomainClass referencedDomainClass = property.referencedDomainClass

                        // Embedded are now always fully rendered
                        if (referencedDomainClass == null || property.embedded || GrailsClassUtils.isJdk5Enum(property.type)) {
                            xml.convertAnother referenceObject
                        } else if (property.oneToOne || property.manyToOne || property.embedded) {
                            asShortObject referenceObject, xml, referencedDomainClass.identifier, referencedDomainClass
                        } else {
                            GrailsDomainClassProperty referencedIdProperty = referencedDomainClass.identifier
                            String refPropertyName = referencedDomainClass.propertyName
                            if (referenceObject instanceof Collection) {
                                for (Object el : referenceObject) {
                                    xml.startNode xml.getElementName(el)
                                    asShortObject el, xml, referencedIdProperty, referencedDomainClass
                                    xml.end()
                                }
                            } else if (referenceObject instanceof Map) {
                                Map<Object, Object> map = (Map<Object, Object>) referenceObject
                                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                                    String key = String.valueOf(entry.key)
                                    xml.startNode("entry").attribute("key", key)
                                    asShortObject entry.value, xml, referencedIdProperty, referencedDomainClass
                                    xml.end()
                                }
                            }
                        }
                    }
                }
            }
            xml.end()
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Checks whether the given domain class property should be included in the
     * XML output.
     *
     * @param prop  the property that should be checked
     * @return      {@code true} if the property should be included in XML
     *              output; {@code false} otherwise
     */
    protected boolean includeInXml(GrailsDomainClassProperty prop) {
        boolean res = false
        try {
            res = isPersistent(prop) ||
                prop.domainClass.clazz.getDeclaredField(prop.name).isAnnotationPresent(XmlExport)
        } catch (NoSuchFieldException e) { /* ignored */ }
        res
    }

    /**
     * Checks whether the given domain class property is persistent.
     *
     * @param prop  the property that should be checked
     * @return      {@code true} if the property is persistent; {@code false}
     *              otherwise
     */
    protected boolean isPersistent(GrailsDomainClassProperty prop) {
        prop.type != Object && prop.persistent && !prop.identity &&
            prop.name != GrailsDomainClassProperty.VERSION
    }
}
