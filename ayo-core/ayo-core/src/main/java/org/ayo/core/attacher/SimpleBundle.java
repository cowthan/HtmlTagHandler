package org.ayo.core.attacher;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/19.
 *
 * fake bundle, the object save in fake bundle doesn't need to be Serializable or Parsable,
 * wherever you get an obejct from fake bundle, it's an refer, not a new object.
 *
 * PS: This class is to instead of Bundle, which is too heavy for common app development.
 */
public class SimpleBundle {

    public static final SimpleBundle EMPTY = new SimpleBundle();

    private Map<String, Object> data = new HashMap<String, Object>();

    public boolean hasExtra(String key){
        return data.containsKey(key);
    }

    public SimpleBundle getExtras(){
        return this;
    }

    public SimpleBundle putExtra(String name, int value){
        data.put(name, new Integer(value));
        return this;
    }
    public SimpleBundle putExtra(String name, float value){
        data.put(name, new Float(value));
        return this;
    }
    public SimpleBundle putExtra(String name, double value){
        data.put(name, new Double(value));
        return this;
    }
    public SimpleBundle putExtra(String name, boolean value){
        data.put(name, new Boolean(value));
        return this;
    }
    public SimpleBundle putExtra(String name, char value){
        data.put(name, new Character(value));
        return this;
    }
    public SimpleBundle putExtra(String name, String value){
        data.put(name, value);
        return this;
    }

    public <T> SimpleBundle putExtra(String name, T t){
        data.put(name, t);
        return this;
    }

    public int getIntExtra(String name){
        Object o = data.get(name);
        return (Integer) o;
    }

    public float getFloatExtra(String name){
        Object o = data.get(name);
        return (Float) o;
    }

    public double getDoubleExtra(String name){
        Object o = data.get(name);
        return (Double) o;
    }

    public boolean getBooleanExtra(String name){
        Object o = data.get(name);
        return (Boolean) o;
    }

    public <T> T getExtra(String name){
        Object o = data.get(name);
        return (T) o;
    }

}
