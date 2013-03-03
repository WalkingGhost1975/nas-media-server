var NasMediaApp = new Marionette.Application();

NasMediaApp.addRegions({
  navigation : '#region-navigation',
  content : '#region-content',
});

NasMediaApp.on('initialize:after', function(){
  Backbone.history.start();
});

