
precision mediump float; 
      	 				
uniform sampler2D u_TextureUnit;
 	 								
uniform vec4 u_Color;			
 	 								
varying vec2 v_TextureCoordinates;      	   								


void main()                    		
{

	vec4 color =  texture2D(u_TextureUnit,v_TextureCoordinates);
	
	color = color * u_Color;
	
	gl_FragColor = color;
  
}