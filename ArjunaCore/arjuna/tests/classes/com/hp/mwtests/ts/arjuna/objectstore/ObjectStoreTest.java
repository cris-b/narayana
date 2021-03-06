/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package com.hp.mwtests.ts.arjuna.objectstore;

/*
 * Copyright (C) 2001,
 *
 * Hewlett-Packard Arjuna Labs,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.
 *
 * $Id: ObjectStoreTest.java 2342 2006-03-30 13:06:17Z  $
 */

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.arjuna.common.arjPropertyManager;
import com.arjuna.ats.arjuna.exceptions.ObjectStoreException;
import com.arjuna.ats.arjuna.objectstore.ObjectStore;
import com.arjuna.ats.arjuna.objectstore.ObjectStoreIterator;
import com.arjuna.ats.arjuna.objectstore.StateStatus;
import com.arjuna.ats.arjuna.objectstore.StateType;
import com.arjuna.ats.arjuna.objectstore.StoreManager;
import com.arjuna.ats.arjuna.state.InputObjectState;
import com.arjuna.ats.arjuna.state.OutputObjectState;
import com.arjuna.ats.arjuna.utils.FileLock;
import com.arjuna.ats.internal.arjuna.objectstore.ActionStore;
import com.arjuna.ats.internal.arjuna.objectstore.CacheStore;
import com.arjuna.ats.internal.arjuna.objectstore.FileLockingStore;
import com.arjuna.ats.internal.arjuna.objectstore.HashedActionStore;
import com.arjuna.ats.internal.arjuna.objectstore.HashedStore;
import com.arjuna.ats.internal.arjuna.objectstore.NullActionStore;
import com.arjuna.ats.internal.arjuna.objectstore.ShadowNoFileLockStore;
import com.arjuna.ats.internal.arjuna.objectstore.ShadowingStore;
import com.arjuna.ats.internal.arjuna.objectstore.VolatileStore;

class DummyOS extends FileLockingStore
{
    public DummyOS(ObjectStoreEnvironmentBean objectStoreEnvironmentBean) throws ObjectStoreException
    {
        super(objectStoreEnvironmentBean);
    }

    public boolean lock ()
    {
        return super.lock(new File("foo"), FileLock.F_WRLCK, true);
    }
    
    public boolean unlock ()
    {
        return super.unlock(new File("foo"));
    }
    
    @Override
    protected InputObjectState read_state (Uid u, String tn, int s)
            throws ObjectStoreException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean remove_state (Uid u, String tn, int s)
            throws ObjectStoreException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean write_state (Uid u, String tn, OutputObjectState buff,
            int s) throws ObjectStoreException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public int typeIs ()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean commit_state (Uid u, String tn) throws ObjectStoreException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public int currentState (Uid u, String tn) throws ObjectStoreException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean hide_state (Uid u, String tn) throws ObjectStoreException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean reveal_state (Uid u, String tn) throws ObjectStoreException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
}

public class ObjectStoreTest
{
    @Test
    public void testActionStore () throws Exception
    {
        ObjectStoreEnvironmentBean objectStoreEnvironmentBean = new ObjectStoreEnvironmentBean();
        objectStoreEnvironmentBean.setLocalOSRoot( "tmp" );

        ActionStore as = new ActionStore(objectStoreEnvironmentBean);
        
        final OutputObjectState buff = new OutputObjectState();
        final String tn = "/StateManager/junit";
        
        for (int i = 0; i < 100; i++)
        {
            Uid u = new Uid();
            
            as.write_uncommitted(u, tn, buff);
            
            as.commit_state(u, tn);
            
            assertTrue(as.currentState(u, tn) != StateStatus.OS_UNCOMMITTED);
            
            InputObjectState ios = new InputObjectState();
            
            as.allObjUids("", ios);
            
            assertTrue(as.read_uncommitted(u, tn) == null);
            
            as.write_committed(u, tn, buff);
            as.read_committed(u, tn);
            
            assertTrue(!as.remove_uncommitted(u, tn));
            
            as.remove_committed(u, tn);
            
            assertTrue(!as.hide_state(u, tn));
            
            assertTrue(!as.reveal_state(u, tn));
        }
    }
    
    @Test
    public void testShadowNoFileLockStore () throws Exception
    {
        ObjectStoreEnvironmentBean objectStoreEnvironmentBean = new ObjectStoreEnvironmentBean();
        objectStoreEnvironmentBean.setLocalOSRoot( "tmp" );

        ShadowNoFileLockStore as = new ShadowNoFileLockStore(objectStoreEnvironmentBean);
        
        final OutputObjectState buff = new OutputObjectState();
        final String tn = "/StateManager/junit";
        
        for (int i = 0; i < 100; i++)
        {
            Uid u = new Uid();
            
            as.write_uncommitted(u, tn, buff);
            
            as.commit_state(u, tn);
            
            assertTrue(as.currentState(u, tn) != StateStatus.OS_UNCOMMITTED);
            
            InputObjectState ios = new InputObjectState();
            
            as.allObjUids("", ios);
            
            assertTrue(as.read_uncommitted(u, tn) == null);
            
            as.write_committed(u, tn, buff);
            as.read_committed(u, tn);
            
            assertTrue(!as.remove_uncommitted(u, tn));
            
            as.remove_committed(u, tn);
            
            assertTrue(!as.hide_state(u, tn));
            
            assertTrue(!as.reveal_state(u, tn));
        }
    }
    
    @Test
    public void testHashedStore () throws Exception
    {
        ObjectStoreEnvironmentBean objectStoreEnvironmentBean = new ObjectStoreEnvironmentBean();
        objectStoreEnvironmentBean.setLocalOSRoot( "tmp" );

        HashedStore as = new HashedStore(objectStoreEnvironmentBean);
        
        final OutputObjectState buff = new OutputObjectState();
        final String tn = "/StateManager/junit";
        
        for (int i = 0; i < 100; i++)
        {
            Uid u = new Uid();
            
            as.write_uncommitted(u, tn, buff);
            
            as.commit_state(u, tn);
            
            assertTrue(as.currentState(u, tn) != StateStatus.OS_UNCOMMITTED);
            
            InputObjectState ios = new InputObjectState();
            
            as.allObjUids("", ios);
            
            assertTrue(as.read_uncommitted(u, tn) == null);
            
            as.write_committed(u, tn, buff);
            as.read_committed(u, tn);
            
            assertTrue(!as.remove_uncommitted(u, tn));
            
            as.remove_committed(u, tn);
            
            assertTrue(!as.hide_state(u, tn));
            
            assertTrue(!as.reveal_state(u, tn));
        }
    }
    
    //@Test
    public void testCacheStore () throws Exception
    {
        ObjectStoreEnvironmentBean objectStoreEnvironmentBean = new ObjectStoreEnvironmentBean();
        objectStoreEnvironmentBean.setLocalOSRoot( "tmp" );

        CacheStore as = new CacheStore(objectStoreEnvironmentBean);
        
        final OutputObjectState buff = new OutputObjectState();
        final String tn = "/StateManager/junit";
        
        for (int i = 0; i < 100; i++)
        {
            Uid u = new Uid();
            
            as.write_uncommitted(u, tn, buff);
            
            as.commit_state(u, tn);
            
            assertTrue(as.currentState(u, tn) != StateStatus.OS_UNCOMMITTED);
            
            InputObjectState ios = new InputObjectState();
            
            as.allObjUids("", ios);
            
            as.read_uncommitted(u, tn);  // may or may not be there if cache flush hasn't happened
            
            as.write_committed(u, tn, buff);
            as.read_committed(u, tn);
            
            as.remove_uncommitted(u, tn);  // may or may not be there if cache flush hasn't happened
            
            as.remove_committed(u, tn);
            
            assertTrue(!as.hide_state(u, tn));
            
            assertTrue(!as.reveal_state(u, tn));
        }
    }
    
    @Test
    public void testHashedActionStore () throws Exception
    {
        ObjectStoreEnvironmentBean objectStoreEnvironmentBean = new ObjectStoreEnvironmentBean();
        objectStoreEnvironmentBean.setLocalOSRoot( "tmp" );

        HashedActionStore as = new HashedActionStore(objectStoreEnvironmentBean);
        
        final OutputObjectState buff = new OutputObjectState();
        final String tn = "/StateManager/junit";
        
        for (int i = 0; i < 100; i++)
        {
            Uid u = new Uid();
            
            as.write_uncommitted(u, tn, buff);
            
            as.commit_state(u, tn);
            
            assertTrue(as.currentState(u, tn) != StateStatus.OS_UNCOMMITTED);
            
            InputObjectState ios = new InputObjectState();
            
            as.allObjUids("", ios);
            
            assertTrue(as.read_uncommitted(u, tn) == null);
            
            as.write_committed(u, tn, buff);
            as.read_committed(u, tn);
            
            assertTrue(!as.remove_uncommitted(u, tn));
            
            as.remove_committed(u, tn);
            
            assertTrue(!as.hide_state(u, tn));
            
            assertTrue(!as.reveal_state(u, tn));
        }
    }
    
    @Test
    public void testShadowingStore () throws Exception
    {
        ObjectStoreEnvironmentBean objectStoreEnvironmentBean = new ObjectStoreEnvironmentBean();
        objectStoreEnvironmentBean.setLocalOSRoot( "tmp" );

        ShadowingStore as = new ShadowingStore(objectStoreEnvironmentBean);
        
        final OutputObjectState buff = new OutputObjectState();
        final String tn = "/StateManager/junit";
        
        for (int i = 0; i < 100; i++)
        {
            Uid u = new Uid();
            
            as.write_uncommitted(u, tn, buff);
            
            as.commit_state(u, tn);
            
            assertTrue(as.currentState(u, tn) != StateStatus.OS_UNCOMMITTED);
            
            InputObjectState ios = new InputObjectState();
            
            as.allObjUids("", ios);
            
            assertTrue(as.read_uncommitted(u, tn) == null);
            
            as.write_committed(u, tn, buff);
            as.read_committed(u, tn);
            
            assertTrue(!as.remove_uncommitted(u, tn));
            
            as.remove_committed(u, tn);
            
            assertTrue(!as.hide_state(u, tn));
            
            assertTrue(!as.reveal_state(u, tn));
        }
    }
    
    @Test
    public void testNullActionStore () throws Exception
    {
        ObjectStoreEnvironmentBean objectStoreEnvironmentBean = new ObjectStoreEnvironmentBean();
        objectStoreEnvironmentBean.setLocalOSRoot( "tmp" );

        NullActionStore as = new NullActionStore(objectStoreEnvironmentBean);
        
        final OutputObjectState buff = new OutputObjectState();
        final String tn = "/StateManager/junit";
        
        for (int i = 0; i < 100; i++)
        {
            Uid u = new Uid();
            
            as.write_uncommitted(u, tn, buff);

            as.commit_state(u, tn);
            
            assertTrue(as.currentState(u, tn) != StateStatus.OS_UNCOMMITTED);
            
            InputObjectState ios = new InputObjectState();
            
            as.allObjUids("", ios);
            
            assertTrue(as.read_uncommitted(u, tn) == null);
            
            as.write_committed(u, tn, buff);
            as.read_committed(u, tn);
            
            assertTrue(!as.remove_uncommitted(u, tn));
            
            as.remove_committed(u, tn);
            
            assertTrue(!as.hide_state(u, tn));
            
            assertTrue(!as.reveal_state(u, tn));
        }
    }

    @Test
    public void testVolatileStore () throws Exception
    {
        ObjectStoreEnvironmentBean objectStoreEnvironmentBean = new ObjectStoreEnvironmentBean();
        objectStoreEnvironmentBean.setLocalOSRoot( "tmp" );

        VolatileStore as = new VolatileStore(objectStoreEnvironmentBean);
        
        final OutputObjectState buff = new OutputObjectState();
        final String tn = "/StateManager/junit";
        
        for (int i = 0; i < 100; i++)
        {
            Uid u = new Uid();
            
            InputObjectState ios = new InputObjectState();
            
            try
            {
                as.allObjUids("", ios);
            }
            catch (final Exception ex)
            {              
            }
            
            try
            {
                assertTrue(as.read_uncommitted(u, tn) == null);
            }
            catch (final Exception ex)
            {
            }
            
            try
            {
                as.commit_state(u, tn);
            }
            catch (final Exception ex)
            {
            }
            
            as.write_committed(u, tn, buff);
            
            assertTrue(as.currentState(u, tn) == StateStatus.OS_COMMITTED);
            
            as.read_committed(u, tn);
            
            try
            {
                assertTrue(as.remove_uncommitted(u, tn));
            }
            catch (final Exception ex)
            {
            }
            
            as.remove_committed(u, tn);
            
            try
            {
                assertTrue(as.hide_state(u, tn));
            }
            catch (final Exception ex)
            {
            }
            
            try
            {               
                assertTrue(as.reveal_state(u, tn));
            }
            catch (final Exception ex)
            {
            }
        }
    }
    
    @Test
    public void testFileLockingStore () throws Exception
    {
        ObjectStoreEnvironmentBean objectStoreEnvironmentBean = new ObjectStoreEnvironmentBean();
        objectStoreEnvironmentBean.setLocalOSRoot( "tmp" );

        DummyOS as = new DummyOS(objectStoreEnvironmentBean);
        
        assertTrue(as.typeIs() != -1);
        
        assertTrue(as.lock());
        assertTrue(as.unlock());
    }
    
    @Test
    public void testIterator () throws Exception
    {
        Uid u1 = new Uid();
        Uid u2 = new Uid();
        
        StoreManager.getTxLog().write_committed(u1, "foo", new OutputObjectState());
        StoreManager.getTxLog().write_committed(u2, "foo", new OutputObjectState());
        
        ObjectStoreIterator iter = new ObjectStoreIterator(StoreManager.getRecoveryStore(), "foo");

        // iteration ordering is not guaranteed.

        Uid x = iter.iterate();
        assertTrue(x.notEquals(Uid.nullUid()));
        assertTrue(x.equals(u1) || x.equals(u2));

        Uid y = iter.iterate();
        assertTrue(y.notEquals(Uid.nullUid()));
        assertTrue(y.equals(u1) || y.equals(u2));

        assertTrue(iter.iterate().equals(Uid.nullUid()));
    }

    private static final boolean validate(ObjectStore objStore)
    {
        if (objStore == null)
            return false;
        
        boolean passed = false;       

        if (objStore.getClass().getName().equals(imple))
        {
            if (objStore.shareState() == StateType.OS_SHARED) {
                if (objStore.storeDir().equals(objectStoreDir)) {
                    if (objStore.storeRoot().equals(localOSRoot))
                        passed = true;
                    else
                        System.err.println("ObjectStore root wrong: " + objStore.storeRoot());
                } else
                    System.err.println("ObjectStore dir wrong: " + objStore.storeDir());
            } else
                System.err.println("Share state wrong: " + objStore.shareState());
        } else
            System.err.println("Implementation wrong: " + objStore.getClass().getSimpleName());

        return passed;
    }

    private static String imple = arjPropertyManager.getObjectStoreEnvironmentBean().getObjectStoreType();
    private static String localOSRoot = "foo";
    private static String objectStoreDir = "tmp"+"/bar";

}
