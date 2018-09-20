package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {

// Example scenarios for testing
//   Item("+5 Dexterity Vest", 10, 20));
//   Item("Aged Brie", 2, 0));
//   Item("Elixir of the Mongoose", 5, 7));
//   Item("Sulfuras, Hand of Ragnaros", 0, 80));
//   Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
//   Item("Conjured Mana Cake", 3, 6));

	@Test
	public void testAgedBrie_SellIn_2to1() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 2, 10) );
		
		// Act
		store.updateEndOfDay(); // SellIn:1, Quality: (11)
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBrie = items.get(0);
		assertEquals("Brie Quality Issue 2-1", 11, itemBrie.getQuality());
		assertEquals("Brie SellIn Issue 2-1", 1, itemBrie.getSellIn());
		
	}
	
	@Test
	public void testAgedBrie_SellIn_1toMinus1() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 1, 10) );
		
		// Act
		store.updateEndOfDay(); // SellIn:0, Quality: +1 (11)
		store.updateEndOfDay(); // SellIn:-1, Quality: +2 (13)
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBrie = items.get(0);
		assertEquals("Brie Quality Issue 1 to -1", 13, itemBrie.getQuality());
		assertEquals("Brie SellIn Issue 1 to -1", -1, itemBrie.getSellIn());
		
	}
	
	@Test
	public void testAgedBrie_QualityOver50() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 1, 49) );
		
		// Act
		store.updateEndOfDay(); // SellIn:0, Quality: +1 (50)
		store.updateEndOfDay(); // SellIn:-1, Quality: (50) (no quality over 50 allowed)
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBrie = items.get(0);
		assertEquals("Brie Quality Issue 1 to -1", 50, itemBrie.getQuality());
		assertEquals("Brie SellIn Issue 1 to -1", -1, itemBrie.getSellIn());
		
	}
	
	@Test
	public void testSulfuras() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80));
		
		// Act
		store.updateEndOfDay(); //Sulfuras should not change in quality or SellIn
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item itemSulfuras = items.get(0);
		assertEquals("Sulfuras Quality Issue", 80, itemSulfuras.getQuality());
		assertEquals("Sulfuras SellIn Issue", 0, itemSulfuras.getSellIn());
	}
	
    
	@Test
	public void testBackstagePass_SellIn_12to9() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 11, 20));
		
		// Act
		store.updateEndOfDay(); // SellIn:11, Quality: +1 (21)
		store.updateEndOfDay(); // SellIn:10, Quality: +2 (23) (+2 increase when SellIn is 10 or less)
		store.updateEndOfDay(); // SellIn:9, Quality: +2 (25)
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBsPass = items.get(0);
		assertEquals("Backstage Pass Quality issue", 26, itemBsPass.getQuality());
		assertEquals("Backstage Pass SellIn issue", 9, itemBsPass.getSellIn());
	}
	
	@Test
	public void testBackstagePass_SellIn_6to4() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 6, 20));
		
		// Act
		store.updateEndOfDay(); // SellIn:5, Quality: +2 (23) (+3 increase when SellIn is 5 or less)
		store.updateEndOfDay(); // SellIn:4, Quality: +2 (26)
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBsPass = items.get(0);
		assertEquals("Backstage Pass Quality issue", 28, itemBsPass.getQuality());
		assertEquals("Backstage Pass SellIn issue", 4, itemBsPass.getSellIn());
	}
	
	@Test
	public void testBackstagePass_SellIn_1toMinus1() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 1, 20));
		
		// Act
		store.updateEndOfDay(); // SellIn:0, Quality: +3 (23) (+3 increase when SellIn is 5 or less)
		store.updateEndOfDay(); // SellIn:-1, Quality: 0 (Quality zero when concert has gone)
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBsPass = items.get(0);
		assertEquals("Backstage Pass Quality issue", 0, itemBsPass.getQuality());
		assertEquals("Backstage Pass SellIn issue", -1, itemBsPass.getSellIn());
	}
	
	@Test
	public void testBackstagePass_QualityOver50() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 5, 48));
		
		// Act
		store.updateEndOfDay(); // SellIn:4, Quality: +3 (50) (+3 increase when SellIn is 5 or less)
		store.updateEndOfDay(); // SellIn:3, Quality: 50 (Quality cannot go over 50)
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBsPass = items.get(0);
		assertEquals("Backstage Pass Quality issue", 50, itemBsPass.getQuality());
		assertEquals("Backstage Pass SellIn issue", 3, itemBsPass.getSellIn());
	}
	
	@Test
	public void testTremendousTankard() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Tremendous Tankard O' Terror", 1, 50));
		
		// Act
		store.updateEndOfDay(); // SellIn:0, Quality: -1 (49)
		store.updateEndOfDay(); // SellIn:-1, Quality: -2 (47)
		
		// Assert
		List<Item> items = store.getItems();
		Item itemTankard = items.get(0);
		assertEquals("Tankard Quality issue", 47, itemTankard.getQuality());
		assertEquals("Tankard SellIn issue", -1, itemTankard.getSellIn());
	}
	
	@Test
	public void testTremendousTankard_QualityLessThan0() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Tremendous Tankard O' Terror", 1, 3));
		
		// Act
		store.updateEndOfDay(); // SellIn:0, Quality: -1 (2)
		store.updateEndOfDay(); // SellIn:-1, Quality: -2 (0)
		store.updateEndOfDay(); // SellIn:-2, Quality: (0) (quality cannot be less than 0)
		
		// Assert
		List<Item> items = store.getItems();
		Item itemTankard = items.get(0);
		assertEquals("Tankard Quality issue", 0, itemTankard.getQuality());
		assertEquals("Tankard SellIn issue", -2, itemTankard.getSellIn());
	}
	
	@Test (expected = Exception.class)
	public void testTremendousTankard_EnterQualityOver50() throws Exception { 
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Tremendous Tankard O' Terror", 1, 55)); //quality over 50 should not be possible
		
	}
	
	@Test
	public void testListSize() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Tremendous Tankard O' Terror", 5, 20));
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
		store.addItem(new Item("Aged Brie", 2, 0));
		store.addItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80));
		
		
		// Act
		store.updateEndOfDay();
		
		
		// Assert
		List<Item> items = store.getItems();
		assertEquals("Tankard Quality issue", 4, items.size());
	}
	
	
	
}
