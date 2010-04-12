APPCFG=$(GAE)/appcfg.py
SPUTNIK_BUNDLE=sputnik.bundle
SERVER=localhost:8080
UPLOAD_COMMAND=$(APPCFG) upload_data --config_file=tools/loader.py --server=$(SERVER)
CLEAN_BULKLOAD=rm bulkloader-*

upload-sputnik: bundle-sputnik upload-sputnik-bundle

bundle-sputnik:
	./tools/bundle.py --root tmp/sputniktests --type sputnik --output $(SPUTNIK_BUNDLE)

upload-sputnik-bundle: upload-sputnik-suite upload-sputnik-cases

upload-sputnik-suite:
	$(UPLOAD_COMMAND) --filename=$(SPUTNIK_BUNDLE) --kind=Suite .
	$(CLEAN_BULKLOAD)

upload-sputnik-cases:
	$(UPLOAD_COMMAND) --filename=$(SPUTNIK_BUNDLE) --kind=Case .
	$(CLEAN_BULKLOAD)
