/*
 *  Copyright 2009, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 

    You may obtain a copy of the License at 

        http://www.apache.org/licenses/LICENSE-2.0 

    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.

 */
package org.docx4j.model.properties.run;

import org.docx4j.XmlUtils;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSValue;

public class Font extends AbstractRunProperty {

	public final static String CSS_NAME = "font-family"; 
	public final static String FO_NAME  = "font-family"; 
	
	private WordprocessingMLPackage wmlPackage;
	
	public Font(WordprocessingMLPackage wmlPackage, RFonts rFonts ) {
		this.setObject(rFonts);
		this.wmlPackage = wmlPackage;
	}

	public Font(CSSValue value) {
		
		debug(CSS_NAME, value);
		log.warn("TODO");
	}
	

	@Override
	public String getCssProperty() {
		
		String font = getPhysicalFont();
		
		if (font!=null) {					
			return composeCss(CSS_NAME, font );
		} else {
			log.error("No mapping from " + font);
			return CSS_NULL;
		}
		
	}

	private String getPhysicalFont() {
		
		RFonts rFonts = (RFonts)this.getObject();
		
		String font = rFonts.getAscii();		
		
		if (font==null) {
			font=wmlPackage.getDefaultFont();
		}
		
		return getPhysicalFont(wmlPackage, font);
		
	}

	public static String getPhysicalFont(WordprocessingMLPackage wmlPackage, String fontName) {

		PhysicalFont pf = wmlPackage.getFontMapper().getFontMappings().get(fontName);
		if (pf!=null) {
			log.debug("Font '" + fontName + "' maps to " + pf.getName() );
			return pf.getName();
		} else {
			log.warn("Font '" + fontName + "' is not mapped to a physical font. " );			
			return null;
		}		
	}	
	
	@Override
	public void setXslFO(Element foElement) {

		String font = getPhysicalFont();
		
		if (font!=null) {					
			foElement.setAttribute(FO_NAME, font );
		} 
		
	}
		

	@Override
	public void set(RPr rPr) {
		rPr.setRFonts((RFonts)this.getObject());
	}
	
	
}