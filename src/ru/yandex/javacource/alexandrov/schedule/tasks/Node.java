package ru.yandex.javacource.alexandrov.schedule.tasks;

 public class Node<T> {

    public T task;
    public Node<T> next;
    public Node<T> prev;

     public Node(T task, Node<T> prev, Node<T> next) {
         this.task = task;
         this.prev = prev;
         this.next = next;
     }
 }