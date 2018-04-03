This README would normally document whatever steps are necessary to get your application up and running.

### Data Mapper ###

* DataMapper is a library, Which tranform the JSON using template. 
* 0.1.SNAPSHOT

### features ###

* rename key
* change value based upon function list defined in template
* handle nested object
* handle array
* group keys
* denormalize array element
* can add extra key-value pair
* convert json to xlsx format

### Setup Guide ###

git clone [git-repo-url] datamapper

### Instruction to use ###

### create a sample JSON : ###

```js
{
  "id": "datamapper",
  "details": [
    {
      "name": "screen",
      "rate": 100.2
    },
    {
      "name": "motherboard",
      "rate": 156.2
    }
  ],
  "data": {
    "values": [1, 2, 3]
  },
  "length": "55",
  "width": "56",
  "height": "57",
  "specification": {
    "dimension": {
      "screensize": "139.7 cm (138.68 cm) (55 [54.6])",
      "Tv": "Approx. 1228 x 711x 86 mm",
      "Box": "Approx. 1366 x 875 x 198 mm"
    },
    "connectivity": {
      "networkStandard": {
        "wireless": {
          "bluetooth": true,
          "wifi": true
        }
      }
    }
  }
}
```
### create Template against the above JSON. ###

```js
{
  "id": {
    "newKeyName": "Identifier",
    "funcList": []
  },
  "source": {
    "newKeyName": "Source",
    "funcList": [],
    "value": "Sony"
  },
  "client": {
    "newKeyName": "Vendor",
    "funcList": [],
    "value": "Amazon"
  },
  "length": {
    "newKeyName": "Length",
    "funcList": [],
    "groupedTo": {"groupKey": "Dimension" , "separator": " x ", "order": 1}
  },
  "width": {
    "newKeyName": "Width",
    "funcList": [],
    "groupedTo": {"groupKey": "Dimension" , "separator": " x ", "order": 2}
  },
  "height": {
    "newKeyName": "Height",
    "funcList": [],
    "groupedTo": {"groupKey": "Dimension" , "separator": " x ", "order": 3}
  },
  "details": {
    "newKeyName": "Details",
    "funcList": [],
    "nested": {
      "name": {
        "newKeyName": "Full Name",
        "funcList": []
      },
      "rate": {
        "newKeyName": "price",
        "funcList": []
      }
    }
  },
  "data": {
    "newKeyName": "Data",
    "funcList": [],
    "nested":{
      "values":{
        "newKeyName": "Screen Size",
        "funcList": []
      }
    }
  },
  "specification": {
    "newKeyName": "Specification",
    "funcList": [],
    "nested": {
      "dimension": {
        "newKeyName": "Dimension",
        "funcList": [],
        "nested": {
          "screensize": {
            "newKeyName": "Screen Size",
            "funcList": []
          }
        }
      },
      "connectivity": {
        "newKeyName": "Connectivity",
        "funcList": [],
        "nested": {
          "networkStandard": {
            "newKeyName": "Network Standard",
            "funcList": [],
            "nested": {
              "wireless": {
                "newKeyName": "Wireless",
                "funcList": [],
                "nested": {
                  "bluetooth": {
                    "newKeyName": "Bluetooth",
                    "funcList": []
                  },
                  "wifi": {
                    "newKeyName": "Wifi",
                    "funcList": []
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
```

### Template creation Rule : ###

For each key-value pair in json common struture 

```js
Template : {
	"key:" {
	"newKeyName": String // Mandatory
	"funcList": [] // Mandatory define the name of the function, which will be applied to the value.
	"value": Any // Optional when key value present in source json else mandatory
	"nested": Map[String, Template] // In case of nested json. 
	"groupedTo": {                                 // Optional,  Use to groupe multiple keys into one key
			"groupedKey": String // Mandatory
			"separator": String // Mandatory
			"order": Int // Mandatory
		}
	}
}
```

#### Datamapper contians an class called vendorJsonMapper. To transform any JSON : ####

` VendorJsonMapper.convert(sourceJson, template) : Will give you tranformed json. `

#### For writing transformed json to xlsx. ####

` XLSConvertor.convert(sourceFileName). `


### Algorithm for tranforming the JSON #

```js
Steps1 : Iterate trough the tempalte
		processObject(sourceData, template)   // template: is the each key template object.
Steps2 : if(key is not present in source) return (template.newKeyName, template.value)
Steps3 :	else if(nested is defined in template) nestedConvert(key, Template, source(template.key))
Steps4 :	else if(key is present in source and its values is Array) processArray(template, source)
Steps5 :	else if(template.groupedTo is defined) (groupedKey, groupedValues)
Steps6 :	else (template.newKeyName, applyFunc on value)
```
