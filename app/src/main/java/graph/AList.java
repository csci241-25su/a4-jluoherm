package graph;

import java.util.NoSuchElementException;

/*
 * Author: James Luo-Hermanson
 * Date: 08/09/2025
 * Purpose: An ArrayList-like dynamic array class that allocates
 * new memory when needed
 */

public class AList<T> {
  protected int size; // number of elements in the AList
  protected T[] a; // the backing array storage

  public int size() {
    return size;
  }

  protected int getCap() {
    return a.length;
  }

  /** Creates an AList with a default capacity of 8 */
  public AList() {
    a = createArray(8);
    size = 0;
  }

  /** Creates an AList with the given capacity */
  public AList(int capacity) {
    a = createArray(capacity);
    size = 0;
  }

  /** Grows a if newSize exceeds a's capacity. The new capacity is the smallest
   * number of doublings of the current capacity needed to exceed newSize. Does
   * nothing if newSize <= a.length.  Grow the array by allocating a new array
   * and copying the old array's contents into the new one. This does *not*
   * change the AList's size. */
  protected void growIfNeeded(int newSize) {
    int currentCap = getCap();

    if (newSize > currentCap){
      int newCap = currentCap;
      //Increase newCap to smallest doubling of current capacity that exceeds newSize
      while (newCap < newSize){
        newCap*=2;
      }

      //Create new array of new capacity, copy previous array into new array
      T[] b = createArray(newCap);
      for (int i = 0; i < currentCap; i++){
        b[i] = a[i];
      }
      a = b;
    }
  }

  /** Resizes the AList.
   *  this *does* modify the size, and may modify the capacity if newsize
   *  exceeds capacity. */
  public void resize(int newsize) {
    size = newsize;
    growIfNeeded(newsize);
  }

  /** Gets element i from AList.
   * @throws IndexOutOfBoundsException if 0 <= i < size does not hold */
  public T get(int i) {
    if (i >= size || i < 0){
      throw new IndexOutOfBoundsException();
    }
    return a[i];
  }

  /** Sets the ith element of the list to value.
   * @throws IndexOutOfBoundsException if 0 <= i < size does not hold */
  public void put(int i, T value) {
    if (i >= size || i < 0){
      throw new IndexOutOfBoundsException();
    }
    a[i] = value;
  }

  /** Appends value at the end of the AList, increasing size by 1.
   * Grows the array if needed to fit the appended value */
  public void append(T value) {
    if (getCap() == size){
      resize(size + 1);
      put(size - 1, value);
    } else put(size++, value);
  }

  /** Removes and returns the value at the end of the AList.
   *  this *does* modify size and cannot modify capacity.
   *  @throws NoSuchElementException if size == 0*/
  public T pop() {
    if (size == 0){
      throw new NoSuchElementException();
    } else {
      //Get last element in array
      //Set value in index to null (remove it), and decrement size
      T value = a[size - 1];
      a[size - 1] = null;
      size--;
      return value;
    }
  }

  /*  Create and return a T[] of size n.
   *  This is necessary because generics and arrays don't play well together.*/
  @SuppressWarnings("unchecked")
  protected T[] createArray(int size) {
    return (T[]) new Object[size];
  }

}
