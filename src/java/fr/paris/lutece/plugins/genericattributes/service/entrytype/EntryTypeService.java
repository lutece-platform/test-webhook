/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.genericattributes.service.entrytype;

import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.GenericAttributeError;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * Abstract implementation of IEntryTypeService
 */
public abstract class EntryTypeService implements IEntryTypeService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestData( Entry entry, HttpServletRequest request, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericAttributeError getResponseData( Entry entry, HttpServletRequest request, List<Response> listResponse, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseValueForExport( Entry entry, HttpServletRequest request, Response response, Locale locale )
    {
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseValueForRecap( Entry entry, HttpServletRequest request, Response response, Locale locale )
    {
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getReferenceListRegularExpression( Entry entry, Plugin plugin )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResponseToStringValue( Entry entry, Response response, Locale locale )
    {
        response.setToStringValueResponse( response.getResponseValue( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericAttributeError canUploadFiles( Entry entry, List<FileItem> listUploadedFileItems, List<FileItem> listFileItemsToUpload, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResponseIterationValue( HttpServletRequest request )
    {
        int nIterationValue = NumberUtils.INTEGER_MINUS_ONE;

        Object objAttributeResponseValue = request.getAttribute( ATTRIBUTE_RESPONSE_ITERATION_NUMBER );
        if ( objAttributeResponseValue != null )
        {
            nIterationValue = (int) objAttributeResponseValue;
        }

        return nIterationValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateEntryReadOnly( )
    {
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateEntryReadOnly( boolean bIsDisplayFront )
    {
        return StringUtils.EMPTY;
    }

    protected void initCommonRequestData( Entry entry, HttpServletRequest request )
    {
        if ( entry.getFields( ) == null )
        {
            entry.setFields( new ArrayList<>( ) );
        }
        String strUsedCorrectResponse = request.getParameter( PARAMETER_USED_CORRECT_RESPONSE );
        createOrUpdateField( entry, FIELD_USED_CORRECT_RESPONSE, null, String.valueOf( strUsedCorrectResponse != null ) );

        String strUsedCompleteResponse = request.getParameter( PARAMETER_USED_COMPLETE_RESPONSE );
        createOrUpdateField( entry, FIELD_USED_COMPLETE_RESPONSE, null, String.valueOf( strUsedCompleteResponse != null ) );
    }

    protected Field createOrUpdateField( Entry entry, String strCode, String strTitle, String strValue )
    {
        Field field = entry.getFieldByCode( strCode );
        if ( field == null )
        {
            field = new Field( );
            field.setCode( strCode );
            field.setParentEntry( entry );
            entry.getFields( ).add( field );
        }
        field.setTitle( strTitle );
        field.setValue( strValue );
        return field;
    }
}
