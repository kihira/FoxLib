package kihira.foxlib.client.gui;

public interface IControl<V> {

    void setValue(V newValue);

    V getValue();
}
