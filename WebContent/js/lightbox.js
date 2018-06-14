// lightbox to work with bootstrap 4
class LightBox 
{
	/*
		locations: array of image address
		modalId: the id of the image tag in the modal
	*/
	constructor(locations, modalId)
	{
		this.locations = locations;
		this.modalId = modalId;
		this.currentSelection = 0;
	}


	nextImage()
	{
		this.currentSelection++;

		if(this.currentSelection >= this.locations.length)
		{
			this.currentSelection = 0;
		}

		document.getElementById(this.modalId).src = this.locations[this.currentSelection];
	}

	previousImage()
	{
		this.currentSelection--;

		if(this.currentSelection < 0)
		{
			this.currentSelection = this.locations.length-1;
		}

		document.getElementById(this.modalId).src = this.locations[this.currentSelection];
	}

	setCurrentImage(obj)
	{
		var curImg = obj.src;
		var i = 0;

		while (i < this.locations.length)
		{
			if(curImg === this.locations[i])
			{
				break;
			}

			i++;
		}
		
		this.currentSelection = i;
		document.getElementById(this.modalId).src = this.locations[this.currentSelection];
	}
}