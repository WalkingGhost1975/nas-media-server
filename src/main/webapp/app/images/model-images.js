NasMediaApp.module('Images', function(Images, App, Backbone, Marionette, $, _) {

    Images.Image = Backbone.Model.extend({
        defaults: {
        },
        initialize: function() {
        }
    });

    Images.ImagesCatalog = App.Catalogs.Catalog.extend({
        defaults: {
            category: 'images'
        }
    });

    Images.ImageCatalogCollection = App.Catalogs.CatalogCollection.extend({
        url: 'repo/images',
        model: Images.ImagesCatalog
    });

});

