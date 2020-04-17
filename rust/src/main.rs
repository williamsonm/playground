#![feature(proc_macro_hygiene, decl_macro)]

#[macro_use] extern crate rocket;
#[macro_use] extern crate rocket_contrib;
#[macro_use] extern crate serde_derive;

#[cfg(test)] mod tests;

use rocket_contrib::json::{Json, JsonValue};

type ID = usize;

#[derive(Serialize, Deserialize)]
struct Message {
    id: Option<ID>,
    contents: String
}

#[get("/", format = "json")]
fn hello() -> Json<Message> {
    Json(Message {
        id: Some(3),
        contents: "whatever".to_string()
    })
}

#[catch(404)]
fn not_found() -> JsonValue {
    json!({
        "status": "error",
        "reason": "Resource was not found."
    })
}

fn rocket() -> rocket::Rocket {
    rocket::ignite()
        .mount("/", routes![hello])
        .register(catchers![not_found])
}

fn main() {
    rocket().launch();
}