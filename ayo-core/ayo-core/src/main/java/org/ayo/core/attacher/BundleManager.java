package org.ayo.core.attacher;

import android.util.SparseArray;

/**
 * Created by Administrator on 2016/1/19.
 */
public class BundleManager {
    private BundleManager(){
        bundles = new SparseArray<SimpleBundle>();
    }

    private static BundleManager instance;

    public static BundleManager getDefault(){
        if(instance == null){
            instance = new BundleManager();
        }
        return instance;
    }

    private SparseArray<SimpleBundle> bundles;

    public int addBundle(SimpleBundle bundle){
        int id = new BundleIdGenerator().getNext();
        bundles.put(id, bundle);
        return id;
    }

    public void removeBundle(int bundleId){
        bundles.remove(bundleId);
    }

    public SimpleBundle getBundle(int bundleId){
        return bundles.get(bundleId);
    }

    public boolean hasBundle(int bundleId){
        return bundles.indexOfKey(bundleId) != -1;
    }

    ///缓存池功能
    public SimpleBundle fetch(){
        return new SimpleBundle();
    }
}
