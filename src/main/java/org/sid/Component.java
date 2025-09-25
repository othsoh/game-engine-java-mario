package org.sid;

import imgui.ImGui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {

    public transient GameObject gameObject = null;

    public void update(float dt){

    }

    public void start(){

    }

    public void imgui(){
        /*try{
            Field[] fields = this.getClass().getDeclaredFields();

            for (Field field: fields){
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());

                if(isPrivate){
                    field.setAccessible(true);
                }

                Class<?> type = field.getType();
                Object value = field.get(this);
                String name = field.getName();
                if(type.equals(int.class)){
                    int val = (int) value;
                    int[] imVal = {val};
                    if(ImGui.dragInt(name+ " : ",imVal)){
                        field.set(this, imVal[0]);
                    }
                }
                if(isPrivate){
                    field.setAccessible(false);
                }
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }*/

        ImGui.text("Called from Component class");
    }

}
