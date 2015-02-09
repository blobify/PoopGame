precision mediump float; 
      	 				
uniform sampler2D u_TextureUnit;		
 	 								
varying vec2 v_TextureCoordinates;
 	 								
uniform float u_Alpha;     	   								


void main()                    		
{
	
	vec4 color =  texture2D(u_TextureUnit,v_TextureCoordinates);
		
	color = color * u_Alpha;
	
	gl_FragColor = color;
  
}