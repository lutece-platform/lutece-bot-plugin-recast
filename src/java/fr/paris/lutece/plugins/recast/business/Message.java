/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.recast.business;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.paris.lutece.plugins.recast.service.BotMessageRenderer;
import fr.paris.lutece.plugins.recast.service.RenderersMap;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Message
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Message extends HashMap<String, Object> implements Serializable
{
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_CARD = "card";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_CONTENT = "content";
    private static final String BEAN_RENDERERS_MAP = "recast.mapRenderers";
    
    private static RenderersMap _mapRenderers;

    /**
     * Returns the Type
     * 
     * @return The Type
     */
    public String getType( )
    {
        return (String) get( FIELD_TYPE );
    }

    /**
     * Returns the Content
     * 
     * @return The Content
     */
    public String getContent( )
    {
        if( _mapRenderers == null )
        {
            _mapRenderers = SpringContextService.getBean( BEAN_RENDERERS_MAP );
        }
        return getContent( _mapRenderers.getMap() );
    }

    /**
     * Returns the Content
     * 
     * @param mapRenderers
     *            renderers
     * @return The Content
     */
    public String getContent( Map<String, BotMessageRenderer> mapRenderers )
    {
        String strContent = "";
        String strType = getType( );
        BotMessageRenderer renderer = mapRenderers.get( strType );
        if ( renderer != null )
        {
            Object content = get( FIELD_CONTENT );
            strContent = renderer.render( content );
        }
        return strContent;
    }

}
