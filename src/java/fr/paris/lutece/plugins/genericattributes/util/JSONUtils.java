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
package fr.paris.lutece.plugins.genericattributes.util;

import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.EntryHome;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.FieldHome;
import fr.paris.lutece.plugins.genericattributes.business.GenericAttributeError;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.EntryTypeServiceManager;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppLogService;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Provides json utility methods for forms
 *rgjkz'�)gj
 */
public final class JSONUtils
{
    /**
     * JSON key for field name
     */
    public static final String JSON_KEY_FIELD_NAME = "field_name";

    /**
     * JSON key to describe a success
     */
    public static final String JSON_KEY_SUCCESS = "success";
    private static final String JSON_KEY_RESPONSE = "response";
    private static final String JSON_KEY_ID_ENTRY = "id_entry";
    private static final String JSON_KEY_ID_RESPONSE = "id_response";
    private static final String JSON_KEY_ID_FIELD = "id_field";
    private static final String JSON_KEY_VALUE_RESPONSE = "value_response";
    private static final String JSON_KEY_FILE_NAME = "file_name";
    private static final String JSON_KEY_ERROR_MESSAGE = "error_message";
    private static final String JSON_KEY_MANDATORY_ERROR = "mandatory_error";
    private static final String JSON_KEY_TITLE_QUESTION = "title_question";
    private static final String JSON_KEY_FORM_ERROR = "form_error";
    private static final String JSON_KEY_MIME_TYPE = "mime_type";
    private static final String JSON_KEY_UPLOADED_FILES = "uploadedFiles";
    private static final String JSON_KEY_FILE_COUNT = "fileCount";

    // PROPERTIES
    private static final String PROPERTY_MESSAGE_ERROR_REMOVING_FILE = "form.message.error.removingFile";

    /**
     * Empty constructor
     */
    private JSONUtils( )
    {
        // nothing
    }

    /**
     * Builds the response
     * 
     * @param json
     *            the json
     * @param locale
     *            the locale
     * @param session
     *            the session
     * @return response the response
     */
    private static Response buildResponse( JSONObject json, Locale locale, HttpSession session )
    {
        Response response = new Response( );
        response.setIdResponse( json.getInt( JSON_KEY_ID_RESPONSE ) );

        Entry entry = EntryHome.findByPrimaryKey( json.getInt( JSON_KEY_ID_ENTRY ) );
        response.setEntry( entry );

        if ( json.containsKey( JSON_KEY_FORM_ERROR ) )
        {
            response.getEntry( ).setError( buildFormError( json.getString( JSON_KEY_FORM_ERROR ) ) );
        }

        if ( json.containsKey( JSON_KEY_VALUE_RESPONSE ) && !json.containsKey( JSON_KEY_FILE_NAME ) )
        {
            response.setResponseValue( json.getString( JSON_KEY_VALUE_RESPONSE ) );
        }

        if ( json.containsKey( JSON_KEY_ID_FIELD ) )
        {
            Field field = FieldHome.findByPrimaryKey( json.getInt( JSON_KEY_ID_FIELD ) );
            response.setField( field );
        }

        // file specific
        boolean bIsFile = false;

        if ( json.containsKey( JSON_KEY_FILE_NAME ) )
        {
            File file = null;

            try
            {
                file = new File( );
                file.setTitle( json.getString( JSON_KEY_FILE_NAME ) );
                file.setMimeType( json.getString( JSON_KEY_MIME_TYPE ) );
            }
            catch( JSONException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }

            response.setFile( file );
            bIsFile = true;
        }

        if ( !bIsFile && ( response.getResponseValue( ) != null ) )
        {
            // if the entry is not a file, we can set the string value
            // data entry as specific behavior
            EntryTypeServiceManager.getEntryTypeService( entry ).setResponseToStringValue( entry, response, locale );
        }

        return response;
    }

    /**
     * Builds the responses list - null if {@link #JSON_KEY_RESPONSE} is missing.
     * 
     * @param strJSON
     *            the json
     * @param locale
     *            the locale
     * @param session
     *            the session
     * @return the responses list - null if {@link #JSON_KEY_RESPONSE} is missing
     */
    @SuppressWarnings( "unchecked" )
    public static Map<Integer, List<Response>> buildListResponses( String strJSON, Locale locale, HttpSession session )
    {
        Map<Integer, List<Response>> mapResponses;
        JSONObject jsonObject = JSONObject.fromObject( strJSON );

        try
        {
            JSON jsonResponses = (JSON) jsonObject.get( JSON_KEY_RESPONSE );

            if ( ( jsonResponses != null ) && !jsonResponses.isEmpty( ) )
            {
                // there is at least one result
                mapResponses = new HashMap<Integer, List<Response>>( );

                if ( jsonResponses.isArray( ) )
                {
                    // array
                    for ( JSONObject jsonResponse : ( (Collection<JSONObject>) ( (JSONArray) jsonResponses ) ) )
                    {
                        Response response = buildResponse( jsonResponse, locale, session );
                        List<Response> listResponses = mapResponses.get( response.getEntry( ).getIdEntry( ) );

                        if ( listResponses == null )
                        {
                            listResponses = new ArrayList<Response>( );
                            mapResponses.put( response.getEntry( ).getIdEntry( ), listResponses );
                        }

                        listResponses.add( response );
                    }
                }
                else
                {
                    // only one response ?
                    JSONObject jsonResponse = (JSONObject) jsonResponses;

                    Response response = buildResponse( jsonResponse, locale, session );

                    List<Response> listResponses = new ArrayList<Response>( );
                    listResponses.add( response );
                    mapResponses.put( response.getEntry( ).getIdEntry( ), listResponses );
                }
            }
            else
            {
                // nothing to do - no response found
                mapResponses = null;
            }
        }
        catch( JSONException jsonEx )
        {
            // nothing to do - response might no be present
            mapResponses = null;
        }

        return mapResponses;
    }

    /**
     * Builds json form {@link GenericAttributeError}
     * 
     * @param formError
     *            {@link GenericAttributeError}
     * @return json string
     */
    public static String buildJson( GenericAttributeError formError )
    {
        JSONObject jsonError = new JSONObject( );

        jsonError.element( JSON_KEY_ERROR_MESSAGE, StringUtils.isNotBlank( formError.getErrorMessage( ) ) ? formError.getErrorMessage( ) : StringUtils.EMPTY );
        jsonError.element( JSON_KEY_MANDATORY_ERROR, formError.isMandatoryError( ) );
        jsonError.element( JSON_KEY_TITLE_QUESTION, formError.getTitleQuestion( ) );

        return jsonError.toString( );
    }

    /**
     * Builds {@link GenericAttributeError} from json string
     * 
     * @param strJson
     *            json string
     * @return the {@link GenericAttributeError}
     */
    public static GenericAttributeError buildFormError( String strJson )
    {
        JSONObject jsonObject = JSONObject.fromObject( strJson );
        GenericAttributeError formError = new GenericAttributeError( );
        formError.setErrorMessage( jsonObject.getString( JSON_KEY_ERROR_MESSAGE ) );
        formError.setMandatoryError( jsonObject.getBoolean( JSON_KEY_MANDATORY_ERROR ) );
        formError.setTitleQuestion( jsonObject.getString( JSON_KEY_TITLE_QUESTION ) );

        return formError;
    }

    /**
     * Builds a json object for the file item list. Key is {@link #JSON_KEY_UPLOADED_FILES}, value is the array of uploaded file.
     * 
     * @param listFileItem
     *            the fileItem list
     * @return the json
     */
    public static JSONObject getUploadedFileJSON( List<FileItem> listFileItem )
    {
        JSONObject json = new JSONObject( );

        if ( listFileItem != null )
        {
            for ( FileItem fileItem : listFileItem )
            {
                json.accumulate( JSON_KEY_UPLOADED_FILES, fileItem.getName( ) );
            }

            json.element( JSON_KEY_FILE_COUNT, listFileItem.size( ) );
        }
        else
        {
            // no file
            json.element( JSON_KEY_FILE_COUNT, 0 );
        }

        return json;
    }

    /**
     * Builds a json object with the error message.
     * 
     * @param request
     *            the request
     * @return the json object.
     */
    public static JSONObject buildJsonErrorRemovingFile( HttpServletRequest request )
    {
        JSONObject json = new JSONObject( );

        json.element( JSONUtils.JSON_KEY_FORM_ERROR, I18nService.getLocalizedString( PROPERTY_MESSAGE_ERROR_REMOVING_FILE, request.getLocale( ) ) );

        return json;
    }

    /**
     * Builds a json object with the error message.
     * 
     * @param json
     *            the JSON
     * @param strMessage
     *            the error message
     */
    public static void buildJsonError( JSONObject json, String strMessage )
    {
        if ( json != null )
        {
            json.accumulate( JSON_KEY_FORM_ERROR, strMessage );
        }
    }
}
