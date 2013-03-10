var NasMediaApp = new Marionette.Application();

NasMediaApp.overlayHandler = [];

NasMediaApp.addRegions({
  navigation : '#region-navigation',
  content : '#region-content',
});

NasMediaApp.on('initialize:after', function(){
  Backbone.history.start();
});

NasMediaApp.vent.on("display:overlay", function() {
    NasMediaApp.overlayHandler.push(window.setTimeout(function() {
        $('#overlay').fadeIn(500);
        $('#overlay').spin('large','grey');
    },500));
});

NasMediaApp.vent.on("hide:overlay", function() {
    window.clearTimeout(NasMediaApp.overlayHandler.pop());
    $('#overlay').fadeOut(400);
});
