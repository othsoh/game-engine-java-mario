package org.sid.jade;

import org.sid.components.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    public static int ID_COUNTER= 0;
    private int uid =-1;

    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

//    public GameObject(String name, int zIndex) {
//        init(name, new Transform(), zIndex);
//    }


    public GameObject(String name, Transform transform, int zIndex) {
        init(name, transform, zIndex);
    }

    private void init(String name, Transform transform, int zIndex){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false: "Error at casting component class";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){
        for (int i=0; i< components.size(); i++){
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void imgui(){
        for (Component c : components){
            c.imgui();
        }
    }

    public void addComponent(Component component){
        component.generateId();
        components.add(component);
        component.gameObject = this;
    }

    public void update(float dt){
        for (Component component : components) {
            component.update(dt);
        }
    }

    public void start(){
        for (Component component : components) {
            component.start();
        }
    }

    public int getzIndex(){
        return this.zIndex;
    }

    public static void init(int id){
        GameObject.ID_COUNTER = id;
    }

    public int getUid(){
        return this.uid;
    }

    public List<Component> getAllComponents(){
        return this.components;
    }
}
