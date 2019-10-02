const selectedSubPageClass = "selectedSubPage";

class SubPageMenu {

	constructor(argumentName, container, autoload = true) {
		this.argumentName = argumentName;
		this.container = container;
		this.autoload = autoload;
		this.items = [];
	}

	addItem(item) {
		this.items.push(item);

		if (this.autoload && !getCurrentUrlParameter(this.argumentName)) {
			this.autoload = false;
			item.updateSubPage();
		}
	}

	createAndAddItem(trigger, listener) {
		this.addItem(new SubPageMenuItem(this, trigger, listener));
	}
	
	unselectAll() {
		for (let item of this.items) {
			item.trigger.removeClass(selectedSubPageClass);
		}
		displayLoading(this.container);
	}
	
	triggerFirst() {
		if (this.items.lenght != 0) {
			this.items[0].updateSubPage();
		}
	}
}

class SubPageMenuItem {
	constructor(subPageMenu, trigger, listener) {
		this.subPageMenu = subPageMenu;
		this.trigger = trigger;
		this.listener = listener;

		let subPageMenuItem = this;

		this.trigger.on("click", function() {
			subPageMenuItem.updateSubPage();
		});

		if (this.trigger.attr("data-subPageKey") == getCurrentUrlParameter(this.subPageMenu.argumentName)) {
			this.updateSubPage();
		}
	}

	updateSubPage() {
		if (!this.trigger.hasClass(selectedSubPageClass)) {
			
			this.subPageMenu.unselectAll();

			this.trigger.addClass(selectedSubPageClass);
			
			let subPageKey = this.trigger.attr("data-subPageKey");

			setParamToCurrentUrl(this.subPageMenu.argumentName, subPageKey);

			let subPageMenuItem = this;

			$.ajax({
				type : "GET",
				url : this.trigger.attr("data-subPageUrl"),
				dataType : "html",
				success : function(data, textStatus, xhr) {
					fillSubPage(data, textStatus, xhr, subPageMenuItem.subPageMenu.container, subPageMenuItem.listener);
				},
				error : function(jqXHR, textStatus, error) {
					handleAjaxError(jqXHR, textStatus, error);
				}
			});
		}
	}
	
}

function fillSubPage(data, textStatus, xhr, container, listener) {
	container.empty();
	container.append(data);

	executeMainActions(container);

	container.find('form').each(function() {
		let form = $(this);
		let submitInput = form.find("input[type='submit']");
		submitInput.on("click", function(e) {
			e.preventDefault();
			displayLoading(container);
			submitForm(form, function(responseData, textStatus, xhr) {
				fillSubPage(responseData, textStatus, xhr, container, listener);
			});
		});
	});

	if (listener != null) {
		listener(data, textStatus, xhr);
	}
}
