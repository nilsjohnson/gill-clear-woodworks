package com.gillccwoodworks;

public class CarouselItem
{
	private String carousel;
	private String address;
	
	public CarouselItem(String carousel, String address)
	{
		this.carousel = carousel;
		this.address = address;
	}
	
	public String getAddress()
	{
		return this.address;
	}
	
	public String getCarousel()
	{
		return this.carousel;
	}
}
