INSERT INTO `mediaserver`.`media_library` (`ID`, `MEDIA_TYPE`, `NAME`) VALUES ('1', 'AUDIO', 'Musikbibliothek');
INSERT INTO `mediaserver`.`media_library` (`ID`, `MEDIA_TYPE`, `NAME`) VALUES ('2', 'IMAGE', 'Urlaubsbilder');

INSERT INTO `mediaserver`.`media_directory` (`ID`, `DIRECTORY`, `FILE_TYPE`, `NAME`, `LIBRARY_ID`) VALUES ('1', 'C:\\Media\\Musik', 'AUDIO', 'Musik', '1');
INSERT INTO `mediaserver`.`media_directory` (`ID`, `DIRECTORY`, `FILE_TYPE`, `NAME`, `LIBRARY_ID`) VALUES ('1', 'C:\\Media\\Bilder', 'IMAGE', 'Bilder', '1');
