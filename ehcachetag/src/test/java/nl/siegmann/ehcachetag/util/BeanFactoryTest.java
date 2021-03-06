package nl.siegmann.ehcachetag.util;

import java.util.Map;

import junit.framework.Assert;

import nl.siegmann.ehcachetag.util.BeanFactory;

import org.junit.Test;

public class BeanFactoryTest {

	static class TestBean {
		private String message;
		private String name;
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	static class SecondTestBean {
		private String color;
		private String size;
		
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
	}

	@Test
	public void testCreateBeansFromProperties_simple() {
		// given
		String input = "messageBean=" + TestBean.class.getName() + "?message=hi&name=jerry\n"
				+ "secondBean=" + SecondTestBean.class.getName() + "?color=red&size=large";

		// when
		Map<String, Object> actualResult = BeanFactory.createBeansFromProperties(input);
		
		// then
		Assert.assertNotNull(actualResult);
		Assert.assertEquals(2, actualResult.size());

		TestBean messageBean = (TestBean) actualResult.get("messageBean");
		Assert.assertNotNull(messageBean);
		Assert.assertEquals("hi", messageBean.getMessage());
		Assert.assertEquals("jerry", messageBean.getName());

		SecondTestBean secondBean = (SecondTestBean) actualResult.get("secondBean");
		Assert.assertNotNull(secondBean);
		Assert.assertEquals("red", secondBean.getColor());
		Assert.assertEquals("large", secondBean.getSize());
	}
	
	@Test
	public void testCreateBeansFromProperties_invalid_property() {
		// given
		String input = "messageBean=" + TestBean.class.getName() + "?message=hi&name=jerry\n"
				+ "secondBean=" + SecondTestBean.class.getName() + "?color=red&XXXsize=large";

		// when
		Map<String, Object> actualResult = BeanFactory.createBeansFromProperties(input);
		
		// then
		Assert.assertNotNull(actualResult);
		Assert.assertEquals(1, actualResult.size());

		TestBean messageBean = (TestBean) actualResult.get("messageBean");
		Assert.assertNotNull(messageBean);
		Assert.assertEquals("hi", messageBean.getMessage());
		Assert.assertEquals("jerry", messageBean.getName());
	}

	@Test
	public void testCreateBeansFromProperties_unknown_class() {
		// given
		String input = "messageBean=" + TestBean.class.getName() + "XXX?message=hi&name=jerry";

		// when
		Map<String, Object> actualResult = BeanFactory.createBeansFromProperties(input);
		
		// then
		Assert.assertNotNull(actualResult);
		Assert.assertEquals(0, actualResult.size());
	}
	
	@Test
	public void testSimpleCase() {
		// given
		String input = TestBean.class.getName() + "?message=hi";
		
		// when
		TestBean testBean = BeanFactory.createBean(input);
		
		// then
		Assert.assertEquals("hi", testBean.getMessage());
		Assert.assertNull(testBean.getName());
	}

	@Test
	public void testSimpleCase2() {
		// given
		String input = TestBean.class.getName() + "?message=hi&name=bob";
		
		// when
		TestBean testBean = BeanFactory.createBean(input);
		
		// then
		Assert.assertEquals("hi", testBean.getMessage());
		Assert.assertEquals("bob", testBean.getName());
	}
}
