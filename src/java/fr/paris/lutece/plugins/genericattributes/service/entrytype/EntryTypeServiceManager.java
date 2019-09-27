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
import fr.paris.lutece.plugins.genericattributes.business.EntryType;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Class to manage entry type services
 */
public final class EntryTypeServiceManager extends AbstractCacheableService
{
    private static final String CACHE_SERVICE_NAME = "Entry Type Service Manager Cache";
    private static EntryTypeServiceManager _instance = new EntryTypeServiceManager( );

    /**
     * Default constructor
     */
    private EntryTypeServiceManager( )
    {
        initCache( );
    }

    /**
     * Get the entry type service associated with an entry
     * 
     * @param entry
     *            The entry to get the entry type service of
     * @return The entry type service, or null if no entry type service was found
     */
    public static IEntryTypeService getEntryTypeService( Entry entry )
    {
        if ( entry != null )
        {
            EntryType entryType = entry.getEntryType( );

            if ( entryType != null )
            {
                IEntryTypeService entryTypeService = (IEntryTypeService) _instance.getFromCache( entryType.getBeanName( ) );

                if ( entryTypeService != null )
                {
                    return entryTypeService;
                }

                entryTypeService = SpringContextService.getBean( entryType.getBeanName( ) );

                synchronized( entryType.getBeanName( ) )
                {
                    _instance.putInCache( entryType.getBeanName( ), entryTypeService );
                }

                return entryTypeService;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return CACHE_SERVICE_NAME;
    }
}
