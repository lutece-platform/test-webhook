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
package fr.paris.lutece.plugins.genericattributes.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;

/**
 *
 * interface IResponseDAO
 *
 */
public interface IResponseDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param response
     *            instance of the Response object to insert
     * @param plugin
     *            the plugin
     */
    void insert( Response response, Plugin plugin );

    /**
     * Load the data of the response from the table
     * 
     * @param nIdResponse
     *            The identifier of the entry
     * @param plugin
     *            the plugin
     * @return the instance of the Entry
     */
    Response load( int nIdResponse, Plugin plugin );

    /**
     * Delete a response from its id
     * 
     * @param nIdResponse
     *            The identifier of the response
     * @param plugin
     *            the plugin
     */
    void delete( int nIdResponse, Plugin plugin );

    /**
     * Update the the response in the table
     * 
     * @param response
     *            instance of the response object to update
     * @param plugin
     *            the plugin
     */
    void store( Response response, Plugin plugin );

    /**
     * Load the data of all the response who verify the filter and returns them in a list
     * 
     * @param filter
     *            the filter
     * @param plugin
     *            the plugin
     * @return the list of response
     */
    List<Response> selectListByFilter( ResponseFilter filter, Plugin plugin );

    /**
     * return a list of statistic on the entry
     * 
     * @param nIdEntry
     *            the id of the entry
     * @param plugin
     *            the plugin
     * @return return a list of statistic on the entry
     */
    List<StatisticEntrySubmit> getStatisticByIdEntry( int nIdEntry, Plugin plugin );

    /**
     * Get the max number from a given id resource
     * 
     * @param nIdEntry
     *            the id of the entry
     * @param plugin
     *            {@link Plugin}
     * @return the max number
     */
    int getMaxNumber( int nIdEntry, Plugin plugin );
}
