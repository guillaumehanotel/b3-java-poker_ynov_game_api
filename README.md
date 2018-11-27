# poker-ynov-game-api


### /game/startingChips

Get minimum amount to start a game.

---
##### ***GET***

**Responses**

| Code | Description |
| ---- | ----------- |
| 200 | return startingChips |
| 500 | Internal server error |

### /users/join

Add a user to a game and returns the game after adding the user.

---
##### ***POST***

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | body | User Id | Yes | number |
| email | body | User Email | Yes | string |
| username | body | User Username | Yes | string |
| password | body | User Password | Yes | string |
| money | body | User Money | Yes | number |

**Responses**

| Code | Description |
| ---- | ----------- |
| 200 | return game |
| 500 | Internal server error |



### /action

Proceed an action and returns game after action process.

---
##### ***POST***

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| gameId | body | Game Id | Yes | number |
| userId | body | User Id | Yes | number |
| actionType | body | Action played (CALL|FOLD|BET) | Yes | string |
| value | body | money bet only if actionType = "BET"  | No | number |

**Responses**

| Code | Description |
| ---- | ----------- |
| 200 | return game |
| 500 | Internal server error |


### /game/{gameId}/users/{userId}/cards

---
##### ***GET***

Returns the downcards of the user for a game.

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| gameId | path | Game Id | Yes | number |
| userId | path | User Id | Yes | number |

**Responses**

| Code | Description |
| ---- | ----------- |
| 200 | return game |
| 500 | Internal server error |

### /game/{gameId}/users/previous/cards

---
##### ***GET***

Returns the downcards of the previous round for the users for a game.

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| gameId | path | Game Id | Yes | number |


**Responses**

| Code | Description |
| ---- | ----------- |
| 200 | return game |
| 500 | Internal server error |

### /game/{gameId}/previous/communitycards

---
##### ***GET***

Returns the community cards of the previous round for a game.

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| gameId | path | Game Id | Yes | number |


**Responses**

| Code | Description |
| ---- | ----------- |
| 200 | return game |
| 500 | Internal server error |