NasMediaApp.module('Images', function(Images, App, Backbone, Marionette, $, _) {

    Images.Image = Backbone.Model.extend({
        defaults: {
        },
        initialize: function() {
        }
    });

    Images.ImagesCatalog = NasMediaApp.Catalogs.Catalog.extend({
        defaults: {
            category: 'images'
        }
    });

    Images.ImageCatalogCollection = NasMediaApp.Catalogs.CatalogCollection.extend({
        url: 'repo/images',
        model: Images.ImagesCatalog
    });

});

