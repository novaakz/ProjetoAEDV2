package dataStructures;

import org.w3c.dom.Node;

import dataStructures.DoubleList.*;

public class OrderedDoubleList<K extends Comparable<K>, V> implements Dictionary<K, V> {

    static final long serialVersionUID = 0L;

    protected DoubleListNode<Entry<K, V>> head;
    protected DoubleListNode<Entry<K, V>> tail;
    protected int currentSize;

    public OrderedDoubleList() {
        head = null;
        tail = null;
        currentSize = 0;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new DoubleListIterator<Entry<K, V>>(head, tail);
    }

    @Override
    public V find(K key) {
        DoubleListNode<Entry<K, V>> node = findNode(key);
        if(node == null)
            return null;
        else if(node.getElement().getKey().compareTo(key) == 0)
            return node.getElement().getValue();
        else
            return null;
    }

    @Override
    public V insert(K key, V value) {
        DoubleListNode<Entry<K, V>> node = findNode(key);
        if(isEmpty()) {
            node = new DoubleListNode<Entry<K,V>>(new EntryClass<K,V>(key, value));
            head = tail = node;
            currentSize++;
            return null;
        }
        else if(node == null) {
            node = new DoubleListNode<Entry<K,V>>(new EntryClass<K,V>(key, value));
            tail.setNext(node);
            node.setPrevious(tail);
            tail = node;
        }
        else if(node.getElement().getKey().compareTo(key) == 0) {
            V old = node.getElement().getValue();
            node.setElement(new EntryClass<K,V>(key, value));
            return old;
        }
        else if(node == head) {
            node = new DoubleListNode<Entry<K,V>>(new EntryClass<K,V>(key, value));
            head.setPrevious(node);
            node.setNext(head);
            head = node;
        }
        else {
            DoubleListNode<Entry<K,V>> nextNode = node;
            DoubleListNode<Entry<K,V>> prevNode = nextNode.getPrevious();
            DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(new EntryClass<K,V>(key, value), prevNode, nextNode);
            prevNode.setNext(newNode);
            nextNode.setPrevious(newNode);
        }
        this.currentSize++;
        return null;
    }

    @Override
    public V remove(K key) {
        DoubleListNode<Entry<K, V>> node = findNode(key);
        if(node == null) {
            return null;
        }
        else if(node.getElement().getKey().compareTo(key) == 0) {
            V value = node.getElement().getValue();
            if(node == head) {
                head = head.getNext();
                if ( head == null )
                    tail = null;
                else
                    head.setPrevious(null);
                currentSize--;
            }
            else if(node == tail) {
                tail = tail.getPrevious();
                if ( tail == null )
                    head = null;
                else
                    tail.setNext(null);
                currentSize--;
            }
            else {
                DoubleListNode<Entry<K, V>> prev = node.getPrevious();
                DoubleListNode<Entry<K, V>> next = node.getNext();
                prev.setNext(next);
                next.setPrevious(prev);
                this.currentSize--;
            }
            return value;
        }
        return null;
    }

    private DoubleListNode<Entry<K, V>> findNode(K key) {
        DoubleListNode<Entry<K, V>> node = head;
        while ( node != null && node.getElement().getKey().compareTo(key) < 0)
        {
            node = node.getNext();
        }
        return node;
    }
}