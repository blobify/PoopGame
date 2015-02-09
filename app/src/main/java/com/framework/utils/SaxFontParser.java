/*package com.framework.utils;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.antibot.poop.Assets;
import com.antibot.poop.Font;
import com.game.framework.gl.TextureRegion;

public class SaxFontParser
{

	public static TextureRegion fontRegion;
	public static int startX,startY;
	
	private static FontDefaultHandler getHandler()
	{
		return new FontDefaultHandler();
	}
	
	
	public static class FontDefaultHandler extends DefaultHandler
	{
		Font font;
		int fontStartId, fontCurrentId;
		boolean fontStartIdSet = false;
		int imageWidth, imageHeight;

		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{

			if (qName.equals("chars"))
			{

				for (int i = 0; i < attributes.getLength(); i++)
				{
					if (attributes.getQName(i).equals("count"))
						font = new Font[Integer.parseInt(attributes.getValue(i)) + 1];
					for (int j = 0; j < font.length; j++)
					{
						font[j] = new Font();
					}
				}

			}

			if (qName.equals("char"))
			{
				int id = 0;
				int x = 0, y = 0, width = 0, height = 0, xAdvance = 0;

				for (int i = 0; i < attributes.getLength(); i++)
				{
					String attributeName = attributes.getQName(i);

					if (attributeName.equals("id"))
					{
						fontCurrentId = Integer.parseInt(attributes.getValue(i));
						if (!fontStartIdSet)
						{
							fontStartId = fontCurrentId;
							fontStartIdSet = true;
						}

						id = fontCurrentId;

						
					}

					else if (attributeName.equals("x"))
					{
						x = Integer.parseInt(attributes.getValue(i));

						
					} else if (attributeName.equals("y"))
					{
						y = Integer.parseInt(attributes.getValue(i));

						
					} else if (attributeName.equals("width"))
					{
						width = Integer.parseInt(attributes.getValue(i));

					} else if (attributeName.equals("height"))
					{
						height = Integer.parseInt(attributes.getValue(i));

					} else if (attributeName.equals("xadvance"))
					{
						xAdvance = Integer.parseInt(attributes.getValue(i));

						
					} else if (attributeName.equals("page"))
					{
						// prepare font object here
						try
						{
							int index = fontCurrentId - fontStartId;
							//font[index].id = id;
							font[index].width = width * 0.01f;
							font[index].height = height * 0.01f;
							//font[index].xAdvance = xAdvance * 0.01f;
							font[index].textureRegion = new TextureRegion(Assets.getTextureFromTextureRegion(fontRegion),startX+x, startY+y, width, height);
						} catch (ArrayIndexOutOfBoundsException e)
						{

						}

					}

				}
			}

			if (qName.equals("common"))
			{

				for (int i = 0; i < attributes.getLength(); i++)
				{
					String attributeName = attributes.getQName(i);

					if (attributeName.equals("scaleW"))
					{
						imageWidth = Integer.parseInt(attributes.getValue(i));

					} else if (attributeName.equals("scaleH"))
					{
						imageHeight = Integer.parseInt(attributes.getValue(i));
					}

				}
			}

		}

		public Font[] getFontArray()
		{
			return font;
		}
	}
	
	public static Font[] parseAndGetFontArray(InputStream stream)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		FontDefaultHandler myHandler = getHandler();
		try{		
			SAXParser saxParser = factory.newSAXParser();		
			saxParser.parse(stream, myHandler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return myHandler.getFontArray();
	}
}
*/