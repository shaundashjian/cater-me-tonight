package controllers;

import java.text.DecimalFormat;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import data.MenuDAO;
import entities.MenuItem;
import entities.OrderItem;
import entities.PlacedOrder;
import pojos.ShoppingCart;

@Controller
public class MenuController {
	
	@Autowired
	private MenuDAO menuDAO;
	
	ShoppingCart myShoppingCart;
	
	public void setMenuDAO(MenuDAO dao) {
		menuDAO = dao;
	}
	
	@RequestMapping(path = "ViewMenuOptions.do", method = RequestMethod.GET)
	public ModelAndView viewMenuOptions(HttpSession session, PlacedOrder placedOrder, int kitchenId) {
		myShoppingCart = new ShoppingCart();
		ModelAndView mv = new ModelAndView();
		mv.addObject("placedOrder", placedOrder);

		if(placedOrder.isAppetizer()){
			System.out.println("in Appetizer items");
			session.setAttribute("appetizerItems", menuDAO.getAllMenuItemsFromAppetizers(kitchenId));	
		}

		if(placedOrder.isEntree()){
			System.out.println("in Entree items");
			session.setAttribute("entreeItems", menuDAO.getAllMenuItemsFromEntrees(kitchenId));
		}
		
		if(placedOrder.isDessert()){
			System.out.println("in dessert items");
			session.setAttribute("dessertItems", menuDAO.getAllMenuItemsFromDesserts(kitchenId));
		}
		
		if(placedOrder.isDrink()){
			System.out.println("in drink items");
			session.setAttribute("drinkItems", menuDAO.getAllMenuItemsFromDrinks(kitchenId));
		}
		
		
		session.setAttribute("shoppingCart", myShoppingCart);
		session.setAttribute("placedOrder", placedOrder);
		session.setAttribute("kitchenId", kitchenId);
		
		
		mv.setViewName("menu");

		return mv;
	}	
	@RequestMapping(path = "ItemsAddedToShoppingCart.do", method = RequestMethod.GET)
	public ModelAndView addItemsToShoppingCart(int menuItemId, int quantity) {
		DecimalFormat df = new DecimalFormat(".##");
		ModelAndView mv = new ModelAndView();
		OrderItem orderItem = new OrderItem();
		MenuItem menuItem = menuDAO.display(menuItemId);
		orderItem.setMenuItem(menuItem);
		orderItem.setQuantity(quantity);
		orderItem.setTotalForOrderItem(menuItem.getPrice()*quantity);
		myShoppingCart.addOrUpdateOrderItem(orderItem);

		mv.setViewName("menu");
		
		return mv;
	}	
	
	
	@RequestMapping (path = "RemoveItemsFromCart.do", method = RequestMethod.GET)
	public ModelAndView removeItemsFromShoppingCart(Integer menuItemId){
		ModelAndView mv = new ModelAndView();
		myShoppingCart.removeItemFromShoppingCart(menuItemId);
		mv.setViewName("menu");
		
		return mv;
	}
	
	
	
	
//	@RequestMapping(path = "AddMenuItemsToCart.do", method = RequestMethod.GET)
//	public ModelAndView addItemsToCart(@RequestParam("kitchenId") int id) {
//		System.out.println("*****************I'm in the add items to cart controller method with item id: "+id+"****************");
//		ModelAndView mv = new ModelAndView();
//		mv.addObject("menuItems", menuDAO.addMenuItemsToCart(id));
//		mv.setViewName("menu");
//		return mv;
//		
//	}


}
