NasMediaApp.module('NasMediaController', function(NasMediaController, App, Backbone, Marionette, $, _){

  NasMediaController.Router = Marionette.AppRouter.extend({
    appRoutes : {
      'home': 'showHome',
      'music': 'showMusic',
      'images': 'showImages',
      'music/*musicPath': 'showMusic',
      'images/*imagePath': 'showImages'
    }
  });

  controller = {

    // Start the app by showing the appropriate views
    start: function(){
      this.showNavigation();
      this.showHome();
    },

    showNavigation: function(){
      var layout = new App.Layout.Navigation();
      App.navigation.show(layout);
    },

    showHome: function(){
      App.vent.trigger("nas-media:navigate", "home");
      var layout = new App.Layout.Home();
      App.content.show(layout);
    },
    showMusic: function(musicPath){
      App.vent.trigger("nas-media:navigate", "music");
      var layout = new App.Layout.Music();
      App.content.show(layout);
    },
    showImages: function(imagePath){
      App.vent.trigger("nas-media:navigate", "images");
      var layout = new App.Layout.Images();
      App.content.show(layout);
    }
  };

  NasMediaController.addInitializer(function() {
    new NasMediaController.Router({
      controller: controller
    });
    controller.start();
  });

});