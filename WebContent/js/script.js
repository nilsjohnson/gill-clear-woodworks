var ukulele_images =
[
	"gillcc_table_legs.jpg",
	"rough_sawn_bench.jpg",
	"board2.jpg",
	"board3.jpg",
	"boards.jpg",
	"table_board.jpg",	
	"table_top.jpg",
	"spalted_maple.jpg",
	"gillcc_table.jpg"
]

var switchTo = function(objectId)
{
	var obj = $('#'+objectId);
	obj.siblings().hide();
	obj.show();
	$('.nav-pills .active').removeClass('active');
	$('#'+objectId+'-link').addClass('active');
	console.log('sup?');
}

var openLightbox = function(obj)
{
	$('#modal-image').attr('src', obj.src);
	$('#lightbox').modal('show');
}

var closeLightbox = function()
{
	$('#lightbox').modal('hide');
}

var switchLightbox = function(imageId)
{
	var source = $('#'+imageId)[0].src;
	if(source.indexOf("back") > 1)
	{
		source = source.replace('back', 'front');
	}
	else
	{
		source = source.replace('front', 'back');
	}	
	$('#' + imageId).attr('src', source);
}

var lightboxChange = function(instrument, step)
{
	var srcStart = "img/";
	var images;
	switch(instrument)
	{
		case "gallery":
			images = ukulele_images;
			break;
						
	}
	var curIndex = 0;
	var sourceStr = $('#modal-image')[0].src;
	var currentImage = sourceStr.split('/').pop();
	for(var i = 0; i < images.length; i++)
	{
		if(currentImage == images[i].split('/').pop())
		{
			curIndex = i;
			break;
		}
	}
	var newIndex = curIndex + step;
	if(newIndex < 0)
	{
		newIndex = images.length - 1;
	}
	else if(newIndex > images.length - 1)
	{
		newIndex = 0;
	}
	
	var srcString = srcStart + images[newIndex];
	$('#modal-image').attr('src', srcString);
}

