BULKLOADER=bulkloader.py
LOADER=loader.py
URL=http://localhost:8080/remote_api
SPUTNIK_ROOT=~/Documents/vms/google/sputniktests/
SESSION_ID:=$(shell whoami)-$(shell date "+%Y.%m.%d-%H.%M.%S")

UPLOAD_CMD=$(BULKLOADER) --config_file=$(LOADER) --url=$(URL) --filename=$(SPUTNIK_ROOT) --app_id=sputnik
UPLOAD_SUITE_CMD=$(UPLOAD_CMD) --loader_opts=session_id:$(SESSION_ID),family:sputnik,name:1

upload:	upload-suite upload-cases

upload-cases:
	$(UPLOAD_SUITE_CMD) --kind=Case

upload-suite:
	$(UPLOAD_SUITE_CMD) --kind=Suite

clean:
	rm -f bulkloader*
