package org.ayo.core.attacher;

import android.util.SparseArray;

/**
 * attacher对象管理，本来attacher对象是通过Intent直接在Activity间传递，但这不还是两个对象吗，
 * 所以通过id传递吧
 * Created by Administrator on 2016/1/25.
 */
public class AttacherManager {

    private AttacherManager(){
        attachers = new SparseArray<ActivityAttacher>();
    }

    private static AttacherManager instance;

    public static AttacherManager getDefault(){
        if(instance == null){
            instance = new AttacherManager();
        }
        return instance;
    }

    private SparseArray<ActivityAttacher> attachers;

    public int addAttacher(ActivityAttacher attacher){
        int id = new BundleIdGenerator().getNext();
        attachers.put(id, attacher);
        return id;
    }

    public void removeAttacher(int id){
        attachers.remove(id);
    }

    public ActivityAttacher getAttacher(int id){
        return attachers.get(id);
    }

    public boolean hasAttacher(int id){
        return attachers.indexOfKey(id) != -1;
    }

}
