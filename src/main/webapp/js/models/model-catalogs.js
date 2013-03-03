NasMediaApp.module('Catalogs', function(Catalogs, App, Backbone, Marionette, $, _){
  Catalogs.Catalog = Backbone.Model.extend({
  });

  Catalogs.CatalogCollection = Backbone.Collection.extend({
    model: Catalogs.Catalog
  });

});