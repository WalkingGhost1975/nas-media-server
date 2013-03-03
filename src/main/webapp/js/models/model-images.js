NasMediaApp.module('Images', function(Images, App, Backbone, Marionette, $, _){

  Images.Image = Backbone.Model.extend({
    defaults: {
    },

    initialize : function() {
    }
  });

  Images.ImageCatalogCollection = NasMediaApp.Catalogs.CatalogCollection.extend({
      url : 'repo/images'
  });

});

