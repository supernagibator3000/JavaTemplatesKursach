package com.example.kursach.services;

import com.example.kursach.models.Item;
import com.example.kursach.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public List<Item> findAll(){
        List<Item> items = itemRepository.findAll();
    //    log.info("findAll() found:" + items);
        return items;
    }

    public void addItem(Item item){
        if(itemRepository.findItemByName(item.getName()) != null){
    //        log.info("addItem() item:" + item + " can't add: item with this name already exists");
            return;
        }
        itemRepository.save(item);
   //     log.info("addItem() item:" + item);
    }

    public void deleteItem(Long id){
        Optional<Item> itemOptional = itemRepository.findById(id);
        if(itemOptional.isPresent()){
            itemRepository.deleteById(id);
    //        log.info("deleteItem() id:" + id + " success");
            return;
        }
    //    log.info("deleteItem() id:" + id + " can't delete: item not found");
    }

    public Item findById(Long id){
        Optional<Item> itemOptional = itemRepository.findById(id);
    //    log.info("findById() id:" + id + " found:" + itemOptional);
        return itemOptional.get();
    }
}
