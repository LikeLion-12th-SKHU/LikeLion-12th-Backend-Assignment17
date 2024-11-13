package org.likelion.itemservice.web.basic;


import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.likelion.itemservice.domain.item.Item;
import org.likelion.itemservice.domain.item.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    /*
    // @RequiredArgsConstructor 사용으로 생략
    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
     */

    // 상품 등록
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    // 같은 url이 오더라도 POST로 오면 save()가 호출됨
    // @PostMapping("/add")
    public String addItemV1(@RequestParam("itemName") String itemMame,
                            @RequestParam("price") int price,
                            @RequestParam("quantity") Integer quantity,
                            Model model) {
        Item item = new Item();
        item.setItemName(itemMame);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        /*
        @ModelAttribute가 자동으로 만들어줌
        Item item = new Item();
        item.setItemName(itemMame);
        item.setPrice(price);
        item.setQuantity(quantity);
         */

        // 첫 글자만 소문자로 바뀜
        itemRepository.save(item);
        return "basic/item";
    }

    // 상품 목록
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    // 상품 상세 추가
    @GetMapping("{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    // 상품 수정
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        /*
        @ModelAttribute가 자동으로 만들어줌
        Item item = new Item();
        item.setItemName(itemMame);
        item.setPrice(price);
        item.setQuantity(quantity);
         */

        // 첫 글자만 소문자로 바뀜
        itemRepository.update(itemId, item);
        return "basic/item";
    }

    /*
    테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }
}
