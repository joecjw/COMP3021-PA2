package hk.ust.comp3021.utils;

public class CustomArrayList<E> {
    private Object[] elements;
    private int size;
    private int capacity;

    /**
     * TODO `CustomArrayList` constructor with default `capacity`(5) 
     * PS: `size` is set to the initial value 0
     */
    public CustomArrayList() {
        this.elements = new Object[5];
        this.size = 0;
        this.capacity = 5;
    }
    
    /**
     * TODO `CustomArrayList` constructor with given `capacity`
     * PS: `size` is set to the initial value 0
     */
    public CustomArrayList(int initialCapacity) {
        this.elements = new Object[initialCapacity];
        this.size = 0;
        this.capacity = initialCapacity;
    }

    /**
     * TODO `add` appends new element into `elements`. Once `size` is equal to `capacity`, 
     * 		we need to resize `elements` to twice its original size.
     * @param element to be added into `elements`
     * @return null
     */
    public void add(E element) {
        if(this.size() + 1 == capacity) {
            resize(capacity * 2);
        }

        elements[this.size()] = element;
        size++;
    }
    
    /**
     * TODO `resize` modifies the size of `elements`
     * @param newCapacity to indicate the new capacity of `elements`
     * @return null
     */
    private void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < this.size(); i++) {
            newArray[i] = this.get(i);
        }
        elements = newArray;
        capacity = newCapacity;
    }

    /**
     * TODO `get` obtains target element based on the given index. Once the index is not within [0, size), 
     * 		we need to return null.
     * @param index to indicate the element position
     * @return element whose index is `index`
     */
    public E get(int index) {
        if(index < 0 || index > this.size() - 1){
            return null;
        }

        return (E) elements[index];
    }

    /**
     * TODO `size` obtains the size of `elements`
     * @param null
     * @return `size`
     */
    public int size() {
        return this.size;
    }
    
    /**
     * TODO `isEmpty` determine whether the list is empty
     * @param null
     * @return boolean variable that indicates the list status
     */
    public boolean isEmpty() {
        if(this.size() == 0){
            return true;
        }
        return false;
    }
    
    /**
     * TODO `contains` determine whether the input is in `elements`
     * @param obj to be determined
     * @return boolean variable that indicates the existence of `obj`
     */
    public boolean contains(E obj) {
        for(int i = 0; i < this.size(); i++){
            if(((E)this.get(i)).equals(obj)){
                return true;
            }
        }
    	return false;
    }
}