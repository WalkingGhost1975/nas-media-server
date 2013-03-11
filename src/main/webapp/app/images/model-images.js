NasMediaApp.module('Images', function(Images, App, Backbone, Marionette, $, _) {

    Images.Image = Backbone.Model.extend({
        defaults: {
            extension : 'jpg'
        },
        initialize : function()  {
            this.set({'extension': this._getExtension()});
        },
        _getExtension : function()  {
            var filename = this.get('name');
            return filename.substr(filename.lastIndexOf('.'));
        }
    });

    Images.ImageCollection = Backbone.Collection.extend({
        model: Images.Image,
        nodeType : 'Image'
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

