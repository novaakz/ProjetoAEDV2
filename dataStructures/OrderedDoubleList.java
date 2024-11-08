package dataStructures;

import dataStructures.DoubleList.*;

public class OrderedDoubleList<K extends Comparable<K>, V> implements Dictionary<K, V> {

    static final long serialVersionUID = 0L;

    protected DoubleList<Entry<K, V>> data;

    public OrderedDoubleList() {
        this.data = new DoubleList<Entry<K, V>>();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return data.iterator();
    }

    @Override
    public V find(K key) {
        DoubleListNode<Entry<K, V>> tmp = findNode(key);
        if(tmp != null) {
            if(tmp.getElement().getKey().compareTo(key) == 0)
                return tmp.getElement().getValue();
            return null;
        }
        else
            return null;
    }

    @Override
    public V insert(K key, V value) {

        DoubleListNode<Entry<K, V>> tmp = findNode(key);
        if(tmp == null) {
            data.addLast(new EntryClass<K, V>(key, value));
            return null;
        }
        else if(tmp.getElement().getKey().compareTo(key) == 0) {
            V old = tmp.getElement().getValue();
            tmp.setElement(new EntryClass<K,V>(key, value));
            return old;
        }
        else {
            if(tmp.equals(data.getNode(0))) {
                EntryClass<K, V> entry = new EntryClass<K,V>(key, value);
                data.addFirst(entry);
            }
            else {
                EntryClass<K, V> entry = new EntryClass<K,V>(key, value);
                int i = 0;
                while(i < data.size() && data.get(i).getKey().compareTo(key) < 0)
                    i++;
                data.addMiddle(i, entry);
            }
            return null;
        }
    }

    @Override
    public V remove(K key) {
        DoubleListNode<Entry<K, V>> tmp = findNode(key);
        V value = tmp.getElement().getValue();
        if(value != null)
            if(tmp.getElement().getKey().compareTo(key) == 0) {
                data.remove(tmp.getElement());
                return value;
            }
        return null;
    }

    private DoubleListNode<Entry<K, V>> findNode(K key) {
        if(data.isEmpty())
            return null;
        int i = 0;
        while(i < data.size() && data.get(i).getKey().compareTo(key) < 0)
            i++;
        if(i == data.size())
            return null;
        return data.getNode(i);
    }
}