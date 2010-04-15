APPCFG=$(GAE)/appcfg.py
SPUTNIK_PATH=tmp/sputniktests
ES5CONFORM_PATH=tmp/es5conform
SERVER=localhost:8080


SPUTNIK_BUNDLE=sputnik.bundle
ES5CONFORM_BUNDLE=es5conform.bundle
UPLOAD_COMMAND=$(APPCFG) upload_data --config_file=tools/loader.py --server=$(SERVER)
CLEAN_BULKLOAD=rm bulkloader-*

upload: upload-sputnik upload-es5conform

upload-sputnik: bundle-sputnik upload-sputnik-bundle

bundle-sputnik:
	./tools/bundle.py --root $(SPUTNIK_PATH) --type sputnik --output $(SPUTNIK_BUNDLE)

upload-sputnik-bundle: upload-sputnik-suite upload-sputnik-cases

upload-sputnik-suite:
	$(UPLOAD_COMMAND) --filename=$(SPUTNIK_BUNDLE) --kind=Suite .
	$(CLEAN_BULKLOAD)

upload-sputnik-cases:
	$(UPLOAD_COMMAND) --filename=$(SPUTNIK_BUNDLE) --kind=Case .
	$(CLEAN_BULKLOAD)

upload-es5conform: bundle-es5conform upload-es5conform-bundle

bundle-es5conform:
	./tools/bundle.py --root $(ES5CONFORM_PATH) --type es5conform --output $(ES5CONFORM_BUNDLE)

upload-es5conform-bundle: upload-es5conform-suite upload-es5conform-cases

upload-es5conform-suite:
	$(UPLOAD_COMMAND) --filename=$(ES5CONFORM_BUNDLE) --kind=Suite .
	$(CLEAN_BULKLOAD)

upload-es5conform-cases:
	$(UPLOAD_COMMAND) --filename=$(ES5CONFORM_BUNDLE) --kind=Case .
	$(CLEAN_BULKLOAD)
