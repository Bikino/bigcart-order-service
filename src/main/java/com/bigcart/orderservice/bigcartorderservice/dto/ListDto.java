package com.bigcart.orderservice.bigcartorderservice.dto;

import java.util.ArrayList;

public class ListDto {
    ArrayList<ItemDto> list = new ArrayList<>();

    public ListDto(ArrayList<ItemDto> list) {
        this.list = list;
    }

    public ListDto() {

    }

    public ArrayList<ItemDto> getList() {
        return list;
    }

    public void setList(ArrayList<ItemDto> list) {
        this.list = list;
    }
    public void addToList(ItemDto itemDto) {
        this.list.add(itemDto);
    }
    public void removeFromList(ItemDto itemDto) {
         this.list.remove(itemDto);
    }

    @Override
    public String toString() {
        return "ListDto{" +
                "list=" + list +
                '}';
    }
}
