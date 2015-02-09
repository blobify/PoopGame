package com.antibot.food;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.Texture;
import com.game.framework.gl.TextureRegion;

public class Font
{
	public  SAXFontParseHandler myHandler;
	
	public float lineHeight;
	
	public Glyph[] glyph;
	
	public Font(Texture texture, float startX, float startY, InputStream fontXmlStream)
	{		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		//if(myHandler == null)  // if creating font handler for first time
		{
			myHandler = new SAXFontParseHandler();			
		}
		
		myHandler.set(texture, startX, startY); 
		
		try{		
			SAXParser saxParser = factory.newSAXParser();	// can be optimized like handler but meehhh	
			saxParser.parse(fontXmlStream, myHandler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public class Glyph
	{		
		public float xAdvance;
		public float xOffset;
		public float yOffset;
		
		public TextureRegion region;
	}
	
	
	
	public class SAXFontParseHandler extends DefaultHandler
	{
		Texture texture;
		float startX, startY;
		
		int fontStartId, fontCurrentId;
		boolean fontStartIdSet;
		
		//Font font;
		
		public void set(Texture texture, float startX, float startY)
		{
			this.texture = texture;
			this.startX = startX;
			this.startY = startY;
			
			//this.font = font; // used for setting lineHeight
			
			fontStartIdSet = false;
			
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			if (qName.equals("chars"))
			{

				for (int i = 0; i < attributes.getLength(); i++)
				{
					if (attributes.getQName(i).equals("count"))
						//font = new Font[Integer.parseInt(attributes.getValue(i)) + 1];
						glyph = new Glyph[Integer.parseInt(attributes.getValue(i)) + 1];
						
					//creating glyph objects
					for (int j = 0; j < glyph.length; j++)
					{
						glyph[j] = new Glyph();
					}
				}

			}

			else if (qName.equals("char"))
			{
				
				int x = 0, y = 0, width = 0, height = 0, xAdvance = 0, xOffset = 0, yOffset = 0;

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

					} 
					else if(attributeName.equals("xoffset"))
					{
						xOffset = Integer.parseInt(attributes.getValue(i));
					}
					else if(attributeName.equals("yoffset"))
					{
						yOffset = Integer.parseInt(attributes.getValue(i));
					}
					
					else if (attributeName.equals("xadvance"))
					{
						xAdvance = Integer.parseInt(attributes.getValue(i));

						
					} else if (attributeName.equals("page"))
					{
						// prepare font object here
						try
						{
							int index = fontCurrentId - fontStartId;
							//font[index].id = id;
							glyph[index].xAdvance = xAdvance * 0.01f;
							glyph[index].xOffset = xOffset * 0.01f;
							glyph[index].yOffset = yOffset * 0.01f;
							glyph[index].region = new TextureRegion(texture,startX+x, startY+y, width, height);
						} catch (ArrayIndexOutOfBoundsException e)
						{
							
						}

					}

				}
			}

			else if (qName.equals("common"))
			{

				for (int i = 0; i < attributes.getLength(); i++)
				{
					String attributeName = attributes.getQName(i);

					if (attributeName.equals("lineHeight"))
					{
						lineHeight = Integer.parseInt(attributes.getValue(i))*0.01f;

					} 
				}
			}

		}
	}
	
	
	// static drawFont method 
	
	public static void drawFont(SpriteBatcher batcher, String fontString, float posX, float posY, Font font, float scale, boolean center)
	{		
		Glyph[] glyph = font.glyph;
				
		float startingPos = posX;
		
		
		if(center = true)
		{ 		
			float textLen = 0;
			float smallNudge = 0;
			for(int i=0; i<fontString.length(); i++)
			{
				int ascii = fontString.charAt(i);
				
				if(ascii == 10)  // end if new line char comes
				{
					break;
				}
				
				else if ( ascii < 32 || ascii > 126)
				{
					continue; // continue if a non drawable character occurs
				}
				ascii -= 32;
				
				textLen += glyph[ascii].xAdvance*scale;
				smallNudge = glyph[ascii].xAdvance/2;
			}
			
			startingPos = posX - (textLen - smallNudge)/2;				
		}
		
		float advancerX = startingPos;  // the cursorX position
		float currentPosY = posY;		// the cursorY position
		
		for (int i = 0; i < fontString.length(); i++) // loop through the entire
														// characters to draw
		{
			int ascii = fontString.charAt(i);
			
			if(ascii == 10) // new line
			{
				currentPosY -= font.lineHeight*scale; // because y axis system inverted in game world
				advancerX = startingPos;
				continue;
			}
			
			else if (ascii < 32 || ascii > 126)
			{
				continue; // continue drawing font if a non drawable character occurs
			}

			ascii -= 32;

			Glyph g = glyph[ascii];
						
			float drawWidth = g.region.drawWidth*scale;
			float drawHeight = g.region.drawHeight*scale;
			
			batcher.drawCarefulSpriteWithPivot(advancerX + g.xOffset*scale, currentPosY-g.yOffset*scale, drawWidth, drawHeight, 0, 1, g.region, WorldRenderer.texShaderProgram);
			
			advancerX += g.xAdvance*scale;
		}
	}

}
