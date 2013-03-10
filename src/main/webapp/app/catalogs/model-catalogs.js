NasMediaApp.module('Catalogs', function(Catalogs, App, Backbone, Marionette, $, _) {
    Catalogs.Catalog = Backbone.Model.extend({
        defaults: {
            active: false,
            category: '',
            name: ''
        },
        initialize : function()  {
            this.set({path : this._path()});
        },
        setActive : function()  {
            this.collection.clearActiveCatalog();
            this.set({'active':true});
        },
        _path : function() {
            return this.get('category') + "/" + this.get('name');
        }
    });

    Catalogs.CatalogCollection = Backbone.Collection.extend({
        initialize : function(models, options)  {
            if (!options || !options.location)  {
                throw new Error("LocationModel not set.");
            }
            this.location = options.location;
            this.listenTo(this.location, 'change', this.syncActiveCatalog);
            this.on('change:active', this._syncLocationModel);
        },
        clearActiveCatalog : function() {
            this.each(function(catalog) {
                catalog.set({active:false},{silent:true});
            });
        },
        syncActiveCatalog : function() {
            this.clearActiveCatalog();
            var location = this.location;
            var activeCatalog = this.find(function(catalog) {
                return catalog.get('name') === location.get('catalog');
            });
            if (activeCatalog) {
                activeCatalog.set({active: true},{silent: true});
            }
        },
        _syncLocationModel: function() {
            var selectedCatalog = this.find(function(catalog) {
                return catalog.get('active');
            });
            this.location.set({'catalog': selectedCatalog.get('name')});
        }
    });

    Catalogs.Node = Backbone.Model.extend({
    });

    Catalogs.NodeCollection = Backbone.Collection.extend({
        model : Catalogs.Node,
        getFolders : function(options)  {
            var folders = new Catalogs.FolderCollection([],options);
            this.chain().filter(function(node) {
                return node.get('@type') === 'Folder';
            }).each(function(folder) {
                folders.add(_.clone(folder.attributes), options);
            });
            return folders;
        },
        getMediaFiles : function(collection, options) {
            var files = this.chain().filter(function(node) {
                return node.get('@type') === collection.nodeType;
            }).each(function(file) {
                collection.add(_.clone(file.attributes), options);
            });
            return files;
        }
    });

    Catalogs.Folder = Backbone.Model.extend({
        defaults : {
            href : '',
            path : ''
        },
        initialize : function()  {
            if (!this.collection || !this.collection.location)  {
                throw new Error('Collection with LocationModel required.');
            }
            this.location = this.collection.location;
            this.set({'href' : this.location.buildHref(this.get('name'))});
            this.set({'path' : this.location.buildPath(this.get('name'))});
        },
        openFolder : function() {
            var path = this.get('path');
            this.location.set('path', path);
        }
    });

    Catalogs.FolderCollection = Backbone.Collection.extend({
        model : Catalogs.Folder,
        initialize : function(models, options)  {
            if (!options || !options.location)  {
                throw new Error('LocationModel required.');
            }
            this.location = options.location;
        }
    });

});