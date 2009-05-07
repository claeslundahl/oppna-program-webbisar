var WebbisWidget = function() {
   
  var obj;
   
  return({
    init: function() {
	  // Find the widget div tag element.
      var widget = document.getElementById('webbis-widget');
      if (widget) {
    	// Store the widget element away.  
        WebbisWidget.obj = widget;
        widget.innerHTML = "loading webbis...";
        
        // Create the script tag
        var script = document.createElement('script');
        script.type = "text/javascript";
        script.src = "http://webbisar.vgregion.se/webbisar/widget.jsonp?callback=WebbisWidget.build"

        // Add the script tag element to the head element.	
        var head = document.getElementsByTagName('head');
        head[0].appendChild(script);
      }
    },
    build: function(response) {
      // Get first webbis in list.
      var webbis = response.webbisar[0];
    
      // Create new widget div element.
      var div = document.createElement('div');
      div.id = "webbis-widget";
    
      // Create image and text divs and append to widget div element.
      var divimg = document.createElement('div');
      divimg.id = "webbis-img";
      var divtxt = document.createElement('div');
      divtxt.id = "webbis-txt";
      div.appendChild(divimg);
      div.appendChild(divtxt);
    
      var link = document.createElement('a');
      link.href = webbis.link;
    
      var img = document.createElement('img');
      img.id = "webbis-image";
      img.src = webbis.img;
      link.appendChild(img);
      divimg.appendChild(link);
            
      var header = document.createElement('div');
      header.id = "webbis-header";
      header.appendChild(document.createTextNode('Webbisar'));
      var link = document.createElement('a');
      link.href = webbis.linkall
      link.appendChild(header)
      
      divtxt.appendChild(link);
      
      var info = document.createElement('p');
          
      var link = document.createElement('a');
      link.href = webbis.link;
      link.appendChild(document.createTextNode(webbis.parents));
      info.appendChild(link);
      info.appendChild(document.createElement('br'));
      divtxt.appendChild(info);
    
      info.appendChild(document.createTextNode(webbis.time));
      info.appendChild(document.createElement('br'));
      divtxt.appendChild(info);
   
      info.appendChild(document.createTextNode(webbis.home));
      divtxt.appendChild(info);

      // Replace the old div tag element with the new one.
      this.obj.parentNode.replaceChild(div,this.obj);
    }
  });
}();

YAHOO.util.Event.onDOMReady(WebbisWidget.init); 
