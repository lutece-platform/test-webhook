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
import fr.paris.lutece.plugins.genericattributes.business.MandatoryError;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.util.GenericAttributesUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * Abstract entry type for selects
 */
public abstract class AbstractEntryTypeSelect extends EntryTypeService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestData( Entry entry, HttpServletRequest request, Locale locale )
    {
        initCommonRequestData( entry, request );
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strCode = request.getParameter( PARAMETER_ENTRY_CODE );
        String strHelpMessage = ( request.getParameter( PARAMETER_HELP_MESSAGE ) != null ) ? request.getParameter( PARAMETER_HELP_MESSAGE ).trim( ) : null;
        String strComment = request.getParameter( PARAMETER_COMMENT );
        String strMandatory = request.getParameter( PARAMETER_MANDATORY );
        String strRoleAssociated = request.getParameter( PARAMETER_ROLE_ASSOCIATED );
        String strCSSClass = request.getParameter( PARAMETER_CSS_CLASS );
        String strOnlyDisplayInBack = request.getParameter( PARAMETER_ONLY_DISPLAY_IN_BACK );
        String strEditableBack = request.getParameter( PARAMETER_EDITABLE_BACK );
        String strIndexed = request.getParameter( PARAMETER_INDEXED );

        String strFieldError = StringUtils.EMPTY;

        if ( StringUtils.isBlank( strTitle ) )
        {
            strFieldError = ERROR_FIELD_TITLE;
        }

        if ( StringUtils.isNotBlank( strFieldError ) )
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, locale )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        entry.setCode( strCode );
        entry.setTitle( strTitle );
        entry.setHelpMessage( strHelpMessage );
        entry.setComment( strComment );
        entry.setCSSClass( strCSSClass );

        entry.setMandatory( strMandatory != null );
        entry.setOnlyDisplayInBack( strOnlyDisplayInBack != null );
        entry.setEditableBack( strEditableBack != null );
        entry.setRoleAssociated( strRoleAssociated != null );
        entry.setIndexed( strIndexed != null );

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericAttributeError getResponseData( Entry entry, HttpServletRequest request, List<Response> listResponse, Locale locale )
    {
        String strIdField = request.getParameter( PREFIX_ATTRIBUTE + entry.getIdEntry( ) );
        int nIdField = -1;
        Field field = null;
        Response response = new Response( );
        response.setEntry( entry );

        if ( StringUtils.isNotEmpty( strIdField ) && StringUtils.isNumeric( strIdField ) )
        {
            nIdField = Integer.parseInt( strIdField );
        }

        if ( nIdField != -1 )
        {
            field = GenericAttributesUtils.findFieldByIdInTheList( nIdField, entry.getFields( ) );
        }

        if ( field != null )
        {
            response.setResponseValue( field.getValue( ) );
            response.setField( field );
        }

        response.setIterationNumber( getResponseIterationValue( request ) );

        listResponse.add( response );

        if ( entry.isMandatory( ) )
        {
            if ( ( field == null ) || StringUtils.isBlank( field.getValue( ) ) )
            {
                return new MandatoryError( entry, locale );
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseValueForExport( Entry entry, HttpServletRequest request, Response response, Locale locale )
    {
        return response.getResponseValue( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseValueForRecap( Entry entry, HttpServletRequest request, Response response, Locale locale )
    {
        if ( response.getField( ) != null )
        {
            return response.getField( ).getTitle( );
        }

        return response.getToStringValueResponse( );
    }
}
