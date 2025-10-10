package org.sid.components;

import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.sid.jade.GameObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {

    public static int ID_COUNTER = 0;
    private int uid = -1;

    public transient GameObject gameObject = null;

    public void update(float dt){

    }

    public void start(){

    }

    public void imgui(){
        try{
            Field[] fields = this.getClass().getDeclaredFields();

            for (Field field: fields){
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());

                if(isPrivate){
                    field.setAccessible(true);
                }
                boolean isTransient = Modifier.isTransient(field.getModifiers());

                if(isTransient){
                    continue;
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
                else if(type.equals(float.class)){
                    float val = (float) value;
                    float[] imVal = {val};
                    if(ImGui.dragFloat(name+ " : ",imVal)){
                        field.set(this, imVal[0]);
                    }
                }
                else if(type.equals(boolean.class)){
                    boolean val = (boolean) value;
//                    boolean[] imVal = {val};
                    if(ImGui.checkbox(name+ " : ",val)){
                        field.set(this, !val);
                    }
                }
                else if(type.equals(Vector3f.class)){
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if(ImGui.dragFloat3(name+ " : ",imVec)){
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                }
                else if(type.equals(Vector4f.class)){
                    Vector4f val = (Vector4f) value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if(ImGui.dragFloat4(name+ " : ",imVec)){
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                }
                if(isPrivate){
                    field.setAccessible(false);
                }
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }

//        ImGui.text("Called from Component class");
    }

    public void generateId(){
        if(this.uid == -1){
            this.uid = ID_COUNTER++;
        }
    }

    public int getUid(){
        return this.uid;
    }

    public static void init(int id){
        Component.ID_COUNTER = id;
    }
}
