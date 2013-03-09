NasMediaApp.module('Model', function(Model, App, Backbone, Marionette, $, _){

  Model.MediaModel = Backbone.Model.extend({
    defaults: {
        page : 'home',
        catalog : '',
        path : ''
    },

    initialize : function() {
    },

    getSelectedCatalog  : function()  {
        var selectedCatalog;
        if (this.catalogs)  {
            selectedCatalog = this.catalogs.find(function(catalog) {
                return catalog.get('active');
            });
        }
        return selectedCatalog;
    },

    syncToCatalogCollection  : function()  {
        if (this.catalogs)  {
            var self = this;
            var activeCatalog = this.catalogs.find(function(catalog) {
                return catalog.get('name') === self.get('catalog');
            });
            if (activeCatalog) {
                activeCatalog.set({active : true})
            }
        }
    },

    syncFromCatalogCollection  : function()  {
        this.set({catalog : this.getSelectedCatalog().get('name')});
    }
  });

});